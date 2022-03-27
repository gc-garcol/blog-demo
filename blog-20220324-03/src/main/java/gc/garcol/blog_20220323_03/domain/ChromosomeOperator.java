package gc.garcol.blog_20220323_03.domain;

import java.util.Arrays;
import java.util.List;

public class ChromosomeOperator {

    /**
     *
     * @param chromosome
     * @param index zero-base
     * @return
     */
    public static void doMutation(Chromosome chromosome, int index) {
        if (index >= chromosome.getLength()) {
            throw new RuntimeException(String.format("Cannot mutate in index %s of chromosome having length %", index, chromosome.getLength()));
        }

        long value = chromosome.getValue();
        long newValue = value ^ (1 << index);
        chromosome.setValue(newValue);
    }

    public static Chromosome createMutation(Chromosome chromosome, int index) {
        if (index >= chromosome.getLength()) {
            throw new RuntimeException(String.format("Cannot mutate in index %s of chromosome having length %", index, chromosome.getLength()));
        }

        long value = chromosome.getValue();
        long newValue = value ^ (1 << index);
        return Chromosome.of(newValue, chromosome.length);
    }

    /**
     *
     * @param father
     * @param mother
     * @param index
     * @return zero-base
     */
    public static List<Chromosome> doCrossover(Chromosome father, Chromosome mother, int index) {
        if (father.getLength() != mother.getLength()) {
            throw new RuntimeException("Invalid length, parent length must be same as mother length");
        }

        int length = father.getLength();

        long rightMark = (1 << (index + 1)) - 1;
        long leftMark = rightMark ^ 0xffffff;

        long leftParent = father.value & leftMark;
        long rightParent = father.value & rightMark;

        long leftMother = father.value & leftMark;
        long rightMother = father.value & rightMark;

        Chromosome firstChild = Chromosome.of(leftParent + rightMother, length);
        Chromosome secondChild = Chromosome.of(leftMother + rightParent, length);

        return Arrays.asList(firstChild, secondChild);
    }

}
