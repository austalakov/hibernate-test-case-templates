package org.hibernate.search.bugs;

import javax.persistence.*;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import java.util.ArrayList;
import java.util.List;

@Entity
@Indexed
public class ItemEntity {

	@Id
	@DocumentId
	private Long id;

	@Field
	private String name;

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
	@IndexedEmbedded
	private List<ItemVersionEntity> versions = new ArrayList<>();

	protected ItemEntity() {
	}

	public ItemEntity(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ItemVersionEntity> getVersions() {
		return versions;
	}

	public void setVersions(List<ItemVersionEntity> versions) {
		this.versions = versions;
	}

}
