package br.com.lupus.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.lupus.models.serializer.Model;
import br.com.lupus.models.serializer.Serializer;

/**
 * <h1>TipoItemTag</h1>
 * 
 * <p>
 * Classe modelo representativa da entidade tag pertencente aos tipos de itens.
 * </p>
 * 
 * @author Mateus A.S
 */
@Entity
@Table(name = "tipo_item_tag")
@JsonSerialize(using = Serializer.class)
public class TipoItemTag extends Model {

	// Construtores

	/**
	 * Construtor vazio
	 */
	public TipoItemTag() {
	}

	/**
	 * Construtor que preenche todos os par�metros do objeto
	 * 
	 * @param id
	 *            par�metro id do objeto TipoItemTag
	 * @param cabecalho
	 *            par�metro cabecalho do objeto TipoItemTag
	 * @param corpo
	 *            par�metro corpo do objeto TipoItemTag
	 * @param tipo
	 *            par�metro tipo do objeto TipoItemTag
	 * @param tipoItem
	 *            par�metro tipoItem do objeto TipoItemTag
	 */
	public TipoItemTag(Long id, String cabecalho, String corpo, String tipo, TipoItem tipoItem) {

		this.id = id;
		this.cabecalho = cabecalho;
		this.corpo = corpo;
		this.tipo = tipo;
		this.tipoItem = tipoItem;
	}

	// Par�metros

	/**
	 * Identifica��o dos diferentes registros de tags. Valor gerado autom�ticamente
	 * ao ser persistido.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "BIGINT UNSIGNED")
	private Long id;

	/** String referente ao nome da tag. Tamanho m�ximo de 50 caracteres. */
	@Column(unique = false, nullable = false, length = 50)
	@NotNull
	private String cabecalho;

	/** String referente ao valor da tag. Tamanho m�ximo de 50 caracteres. */
	@Column(unique = false, nullable = true, length = 50)
	private String corpo;

	/**
	 * String referente ao tipo de dado que o valor da tag representa (como data,
	 * n�mero, etc). Tamanho m�ximo de 50 caracteres.
	 */
	@Column(unique = false, nullable = true, length = 50)
	@JsonIgnore
	private String tipo;

	/** TipoItem a qual a tag pertence. */
	@ManyToOne(optional = false)
	@JoinColumn(name = "tipo_item_id")
	private TipoItem tipoItem;

	// Getters & Setters

	/**
	 * M�todo que retorna o n�mero de identifica��o do registro TipoItemTag
	 * 
	 * @return id da tag
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Define um n�mero de identifica��o para o objeto TipoItemTag (Sempre, ao ser
	 * persistido, o n�mero de identifica��o � gerado automaticamente)
	 * 
	 * @param id
	 *            n�mero de identifica��o
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * M�todo que retorna o nome do objeto TipoItemTag (cabe�alho)
	 * 
	 * @return cabe�alho da tag
	 */
	public String getCabecalho() {
		return cabecalho;
	}

	/**
	 * Define um nome para o objeto TipoItemTag (cabe�alho)
	 * 
	 * @param cabecalho
	 *            nome a ser colocado na tag
	 */
	public void setCabecalho(String cabecalho) {
		this.cabecalho = cabecalho;
	}

	/**
	 * M�todo que retorna o valor do objeto TipoItemTag (corpo)
	 * 
	 * @return corpo da tag
	 */
	public String getCorpo() {
		return corpo;
	}

	/**
	 * Define um nome para o objeto TipoItemTag (cabe�alho)
	 * 
	 * @param cabecalho
	 *            nome a ser colocado na tag
	 */
	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}

	/**
	 * M�todo que retorna o tipo do objeto TipoItemTag
	 * 
	 * @return tipo de dado do valor da tag
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * Define um tipo para o objeto TipoItemTag
	 * 
	 * @param tipo
	 *            tipo de dado do valor da tag
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * M�todo que retonar o TipoItem a qual a tag est� anexada.
	 * 
	 * @return objeto TipoItem a qual a tag est� anexada.
	 */
	public TipoItem getTipoItem() {
		return tipoItem;
	}

	/**
	 * Define a qual TipoItem a tag � relacionada.
	 * 
	 * @param tipoItem
	 *            objeto TipoItem (necessita ser populado com o registro de TipoItem
	 *            j� existente e n�o pendente a existir, com o campo id correto).
	 */
	public void setTipoItem(TipoItem tipoItem) {
		this.tipoItem = tipoItem;
	}
}
