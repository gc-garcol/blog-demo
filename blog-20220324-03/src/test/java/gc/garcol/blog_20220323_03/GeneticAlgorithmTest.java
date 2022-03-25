package gc.garcol.blog_20220323_03;

import gc.garcol.blog_20220323_03.domain.Chromosome;
import gc.garcol.blog_20220323_03.domain.GeneticAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class GeneticAlgorithmTest {

    @Test
    public void test() {
        double lowerBound = -1;
        double upperBound = 2;
        int totalBit = 22;
        int totalFragment = 1 << totalBit;

        Function<Chromosome, Double> eval = eval(lowerBound, upperBound, totalFragment);

        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(eval, totalBit, 50, 0.25, 0.01);
        Chromosome chromosome = geneticAlgorithm.getBestChromosomes(1000);
        double x = convertToRealVariable(chromosome, lowerBound, upperBound, 1 << totalBit);
        System.out.println(x);
        System.out.println(f(x));
    }

    private Function<Chromosome, Double> eval(double lowerBound, double upperBound, int totalFragment) {
        return (Chromosome chromosome) -> {
            return f(convertToRealVariable(chromosome, lowerBound, upperBound, totalFragment));
        };
    }

    private double convertToRealVariable(Chromosome chromosome, double lowerBound, double upperBound, int totalFragment) {
        return lowerBound + chromosome.getValue() * (upperBound - lowerBound) / totalFragment;
    }

    private double f(double x) {
        return x * Math.sin(10 * Math.PI * x) + 1;
    }



}
