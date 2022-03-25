package gc.garcol.blog_20220323_03.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.SecureRandom;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

@Getter
public class RouletteWheel {

    double[] statistic;

    Function<Chromosome, Double> evaluation;

    private SecureRandom random;

    public RouletteWheel(int size, Function<Chromosome, Double> evaluation) {
        statistic = new double[size];
        this.evaluation = evaluation;
        random = new SecureRandom();
    }

    public void recreateRouletteWheel(List<Chromosome> chromosomes) {
        statistic = new double[chromosomes.size()];
        DoubleWrapper totalEvaluation = new DoubleWrapper(0);
        IntStream.range(0, chromosomes.size())
            .forEach(i -> {
                statistic[i] = Math.pow(Math.E, evaluation.apply(chromosomes.get(i)));
                totalEvaluation.increase(statistic[i]);
                if (i != 0) {
                    statistic[i] += statistic[i - 1];
                }
            });
        for (int i = 0; i < statistic.length; i++) {
            statistic[i] = statistic[i] / totalEvaluation.value;
        }
    }

    public int rotateRouletteWheel() {
        double wheelValue = random.nextDouble() * 0.9999999f;
        for (int i = 0; i < statistic.length; i++) {
            if (wheelValue < statistic[i]) {
                return i;
            }
        }
        throw new RuntimeException("Some error in rotating Roulette Wheel");
    }



}

@AllArgsConstructor
class DoubleWrapper {
    double value;

    void reset() {
        this.value = 0;
    }

    void increase(double inc) {
        value += inc;
    }

}
