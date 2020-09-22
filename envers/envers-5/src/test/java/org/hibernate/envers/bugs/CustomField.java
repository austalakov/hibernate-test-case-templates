package org.hibernate.envers.bugs;

import com.sun.istack.internal.NotNull;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Audited
public class CustomField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_name")
    @NotNull
    private String fieldName;

    @Column(name = "field_value")
    @NotNull
    private String fieldValue;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, targetEntity= Item.class)
    @JoinColumn(name = "item_guid", referencedColumnName = "guid")
    private Item item;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
