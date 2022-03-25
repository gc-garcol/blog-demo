package gc.garcol.blog_20220323_03.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor(staticName = "of")
@Getter
@Setter
public class Chromosome {

    long value;

    int length;

    public Chromosome(Chromosome clone) {
        this.value = clone.value;
        this.length = clone.length;
    }

    private Chromosome(String binaryString) {
        value = Long.parseLong(binaryString, 2);
        length = binaryString.length();
    }

    public String toBinary() {
        return Long.toBinaryString(value);
    }

}
