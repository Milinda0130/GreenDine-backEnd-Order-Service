    package edu.icet.ecom.entity;

    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;

    import java.util.List;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "menu_items")
    public class MenuItemEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String name;
        @Column(length = 2000)
        private String description;
        private Double price;
        private String category;
        private String image;
        private Boolean available;
        @ElementCollection
        @CollectionTable(name = "menu_item_dietary", joinColumns = @JoinColumn(name = "menu_item_id"))
        @Column(name = "dietary")
        private List<String> dietary;
        @ElementCollection
        @CollectionTable(name = "menu_item_ingredients", joinColumns = @JoinColumn(name = "menu_item_id"))
        @Column(name = "ingredient")
        private List<String> ingredients;

    }
