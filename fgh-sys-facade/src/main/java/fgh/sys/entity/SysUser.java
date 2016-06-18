package fgh.sys.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class SysUser implements Serializable {

	private static final long serialVersionUID = -3106703828790319654L;

	@NotNull
	private String id;

	@JsonProperty
	@XmlElement(name = "name")
	@NotNull
	@Size(min = 6, max = 50)
	private String name;

	public SysUser() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
