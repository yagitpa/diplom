package ru.skypro.homework.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ads")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdsDao {

    public static final int MAX_TITLE_LENGTH = 100;
    public static final int MAX_IMAGE_PATH_LENGTH = 512;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    @Column(name = "title", nullable = false, length = MAX_TITLE_LENGTH)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "image", length = MAX_IMAGE_PATH_LENGTH)
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UsersDao author;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentsDao> commentsDaos = new ArrayList<>();
}