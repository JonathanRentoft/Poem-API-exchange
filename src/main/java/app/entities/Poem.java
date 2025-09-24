package app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "poems")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Poem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "author", length = 100)
    private String author;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    public Poem(String title, String author, String content) {
        this.title = title;
        this.author = author;
        this.content = content;
    }
}
