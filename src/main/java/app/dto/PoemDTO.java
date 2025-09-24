package app.dto;

import app.entities.Poem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PoemDTO {
    private int id;
    private String title;
    private String author;
    private String content;

    // Constructor to convert an Entity to a DTO
    public PoemDTO(Poem poem) {
        this.id = poem.getId();
        this.title = poem.getTitle();
        this.author = poem.getAuthor();
        this.content = poem.getContent();
    }
}
