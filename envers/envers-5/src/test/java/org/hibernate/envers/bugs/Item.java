package org.hibernate.envers.bugs;

import com.sun.istack.internal.NotNull;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
@Audited
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String guid;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKey(name="fieldName")
    private Map<String, CustomField> customFields = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public Map<String, CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, CustomField> customFields) {
        this.customFields = customFields;
    }
}
