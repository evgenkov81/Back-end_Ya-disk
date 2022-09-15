package ru.evgenkov.beckend_yadisk.model;


import lombok.*;
import org.hibernate.Hibernate;
import ru.evgenkov.beckend_yadisk.components.shemas.SystemItemType;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "elements")
public class Element {
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    @Column
    private String url;
    @Column
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Element parent;
    @Column
    private SystemItemType type;
    @Column
    private Long size;
    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    @ToString.Exclude
    private List<Element> children = new ArrayList<>();

    public Element(String id, String url, LocalDateTime date, SystemItemType type, Long size) {
        this.id = id;
        this.url = url;
        this.date = date;
        this.type = type;
        this.size = size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Element element = (Element) o;
        return id != null && Objects.equals(id, element.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


    }
