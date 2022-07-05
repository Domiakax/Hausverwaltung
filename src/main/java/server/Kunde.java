package server;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@XmlRootElement
@NoArgsConstructor
@JsonTypeName(value="kunde")
public class Kunde {
	@JsonProperty
	private int kdnr;
	@JsonProperty
	private String name, vorname;

	public Kunde(String name, String vorname) {
		this.name = name;
		this.vorname = vorname;
	}
}
