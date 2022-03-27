package gc.garcol.blog_20220323_03.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneticAlgorithm {

    private int chromosomeLength;
    private int chromosomesSize;
    private List<Chromosome> chromosomes;
    private RouletteWheel rouletteWheel;

    private Function<Chromosome, Double> function;

    private double crossoverFraction;
    private double mutationFraction;

    private static final SecureRandom random = new SecureRandom();

    public GeneticAlgorithm(Function<Chromosome, Double> function, int chromosomeLength, int chromosomesSize, double crossoverFraction, double mutationFraction) {
        this.chromosomeLength = chromosomeLength;
        this.chromosomesSize = chromosomesSize;
        this.crossoverFraction = crossoverFraction;
        this.mutationFraction = mutationFraction;
        this.function = function;
        rouletteWheel = new RouletteWheel(chromosomesSize, function);
    }

    public Chromosome getBestChromosomes(int numberOfCycle) {
        generatePopulation();

        IntStream.range(0, numberOfCycle).forEach(i -> evolve());
        Chromosome bestFit =
            chromosomes.stream()
                .max((first, second) -> Double.compare(function.apply(first), function.apply(second)))
                .orElseThrow();
        return bestFit;
    }

    private void evolve() {
        doSelection();
        doCrossovers();
        doMutations();
    }

    private void generatePopulation() {
        long maxValue = (1 << chromosomeLength) - 1;
        chromosomes = IntStream.range(0, chromosomesSize)
            .mapToObj(i -> Chromosome.of((long)(random.nextDouble() * maxValue), chromosomeLength))
            .collect(Collectors.toList());
    }

    private void doSelection() {
        rouletteWheel.recreateRouletteWheel(chromosomes);

        List<Chromosome> strongGeneration = new ArrayList<>(chromosomesSize);
        for (int i = 0; i < chromosomesSize; i++) {
            int pickedChromosomeIndex = rouletteWheel.rotateRouletteWheel();
            strongGeneration.add(new Chromosome(chromosomes.get(pickedChromosomeIndex)));
        }

        chromosomes.clear();
        chromosomes.addAll(strongGeneration);
    }

    private void doCrossovers() {

        List<Chromosome> crossovers = new ArrayList<>();
        List<Chromosome> newGeneration = new ArrayList<>();
//        List<Chromosome> oldGeneration = new ArrayList<>();

        for (int i = 0; i < chromosomesSize; i++) {
            if (random.nextDouble() <= crossoverFraction) {
                crossovers.add(chromosomes.get(i));
            }
        }

        int crossoversSize = crossovers.size();
        for (; crossoversSize > 1; ) {
            int fatherIndex = (int)(random.nextDouble() * crossoversSize);
            Chromosome father = crossovers.get(fatherIndex);
            crossovers.remove(fatherIndex);
            crossoversSize--;

            int motherIndex = (int)(random.nextDouble() * crossoversSize);
            Chromosome mother = crossovers.get(motherIndex);
            crossovers.remove(motherIndex);
            crossoversSize--;

            int randomIndex = (int)(random.nextDouble() * (chromosomeLength - 1));

            List<Chromosome> newChildren = ChromosomeOperator.doCrossover(father, mother, randomIndex);
            newGeneration.addAll(newChildren);
//            oldGeneration.add(father);
//            oldGeneration.add(mother);
        }

//        chromosomes.removeAll(oldGeneration);
        chromosomes.addAll(newGeneration);
        chromosomes.size();
    }

    private void doMutations() {
        for (int i = 0; i < chromosomesSize; i++) {
            for (int j = 0; j < chromosomeLength; j++) {
                boolean shouldMutation = random.nextDouble() < mutationFraction;
                if (shouldMutation) {
//                    ChromosomeOperator.doMutation(chromosomes.get(i), j);
                    chromosomes.add(ChromosomeOperator.createMutation(chromosomes.get(i), j));
                }
            }
        }
    }

}
