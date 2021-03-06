package br.com.lupus.controllers;

import java.util.HashMap;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lupus.exceptions.ConflictException;
import br.com.lupus.exceptions.EntityNotFound;
import br.com.lupus.exceptions.UnprocessableEntityException;
import br.com.lupus.models.Ambiente;
import br.com.lupus.models.Item;
import br.com.lupus.models.Movimentacao;
import br.com.lupus.models.TipoItem;
import br.com.lupus.models.Usuario;
import br.com.lupus.services.ItemService;
import br.com.lupus.services.MovimentacaoService;
import br.com.lupus.utils.BindingResultUtils;

/**
 * Controller com os end-points relacionados ao CRUD de itens no sistema
 * 
 * @author Mateus A.S
 */
@RestController
@RequestMapping("/api/v1/item")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private MovimentacaoService movimentacaoService;

	/**
	 * End-point de URL /api/v1/item - Retorna ao cliente que fez a requisi��o um
	 * array de objetos JSON representando os itens cadastrados no sistema
	 * 
	 * @return ResponseEntity populado com os itens cadastrados no sistema
	 */
	@GetMapping
	private ResponseEntity<Object> listarItens() {
		try {
			// 200 - OK
			Item.setParametros(new Item(), "id", "tipo");
			TipoItem.setParametros(new TipoItem(), "id", "nome");
			return ResponseEntity.ok(itemService.buscarItens());
		} catch (Exception e) {
			// 500 - INTERNAL SERVER ERROR
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * End-point de URL /api/v1/item/{id do item procurado} - Retorna ao cliente que
	 * fez a requisi��o um objeto JSON representando o item solicitado
	 * 
	 * @param id
	 *            id presente na URL para procura do item
	 * @return ResponseEntity populado com o item solicitado
	 */
	@GetMapping("/{id}")
	private ResponseEntity<Object> buscarItem(@PathVariable Long id) {
		try {
			// 200 - OK
			Item.setParametros(new Item(), "id", "tipo", "cadastrante", "ambienteAtual");
			TipoItem.setParametros(new TipoItem(), "id", "nome");
			Usuario.setParametros(new Usuario(), "id", "nome", "email", "ativo");
			Ambiente.setParametros(new Ambiente(), "id", "descricao");
			return ResponseEntity.ok(itemService.buscar(id));
		} catch (EntityNotFound e) {
			// 404 - NOT FOUND
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			// 500 - INTERNAL SERVER ERROR
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * End-point de URL /api/v1/item - Recebe um objeto JSON no corpo da requisi��o
	 * representando um item a ser cadastrado, o end-point valida ele e tenta
	 * persistir, se o processo ocorrer com sucesso retorna um status 204
	 * 
	 * @param item
	 *            item a ser cadastrado
	 * @param brItem
	 *            objeto populado com os poss�veis erros de valida��o
	 * @return ResponseEntity com status 202
	 */
	@PostMapping
	public ResponseEntity<Object> cadastrarItem(@Valid @RequestBody Item item, BindingResult brItem) {
		try {
			// 202 - OK / NO CONTENT
			itemService.cadastrar(item, brItem);
			return ResponseEntity.noContent().build();
		} catch (UnprocessableEntityException e) {
			// 422 - UNPROCESSABLE ENTITY
			return ResponseEntity.unprocessableEntity().body(BindingResultUtils.toHashMap(brItem));
		} catch (Exception e) {
			// 500 - INTERNAL SERVER ERROR
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * End-point de URL /api/v1/item/{id do item a ser editado} - Recebe no
	 * corpo da requisi��o um objeto JSON representando um item a ser editado, o
	 * end-point valida ele e tenta editar no id informado na url da requisi��o, se
	 * o processo ocorrer com sucesso retorna o item editado
	 * 
	 * @param id
	 *            id do item a ser editado
	 * @param item
	 *            objeto item com os valores a serem alterados
	 * @return ResponseEntity populado com o item editado com status 202 (OK -
	 *         NO CONTENT), 404 (NOT FOUND), 422 (UNPROCESSABLE ENTITY) ou 500
	 *         (INTERNAL SERVER ERROR)
	 */
	@PutMapping("/{id}")
	public ResponseEntity<Object> editarItem(@PathVariable Long id, @Valid @RequestBody Item item,
			BindingResult brItem) {
		try {
			// 200 - OK
			item.setId(id);
			Item.setParametros(new Item(), "id", "tipo", "cadastrante", "ambienteAtual");
			TipoItem.setParametros(new TipoItem(), "id", "nome");
			Usuario.setParametros(new Usuario(), "id", "nome", "email", "permissao", "ativo");
			Ambiente.setParametros(new Ambiente(), "id", "descricao");
			return ResponseEntity.ok(itemService.atualizar(item, brItem));
		} catch (EntityNotFound e) {
			// 404 - NOT FOUND
			return ResponseEntity.notFound().build();
		} catch (UnprocessableEntityException e) {
			// 422 - UNPROCESSABLE ENTITY
			return ResponseEntity.unprocessableEntity().body(BindingResultUtils.toHashMap(brItem));
		} catch (Exception e) {
			// 500 - INTERNAL SERVER ERROR
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * End-point de URL /api/v1/item/{id do item a ser deletado} - Recebe na URL o
	 * id de um item a ser excluido, retornando no ResponseEntity o status 202 se
	 * tudo ocorrer bem, 404 se o item referente n�o existir e 500 para outros
	 * poss�veis erros n�o tratados
	 * 
	 * @param id
	 *            id do item a ser excluido
	 * @return ResponseEntity com status 202 (OK - NO CONTENT), 404 (NOT FOUND), 422
	 *         (UNPROCESSABLE ENTITY) ou 500 (INTERNAL SERVER ERROR)
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Object> deletarItem(@PathVariable Long id){
		try {
			// 202 - OK / NO CONTENT
			itemService.deletarItem(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFound e) {
			// 404 - NOT FOUND
			return ResponseEntity.notFound().build();
		} catch (Exception e) {
			// 500 - INTERNAL SERVER ERROR
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PatchMapping("/{itemId}/movimenta/{ambienteId}")
	public ResponseEntity<Object> movimentar(@PathVariable Long itemId, @PathVariable Long ambienteId) {
		try {
			Movimentacao.setParametros(new Movimentacao(), "id", "dataMovimentacao", "itemMovimentado", "ambienteAnterior", "ambientePosterior", "movimentador");
			Item.setParametros(new Item(), "id");
			Ambiente.setParametros(new Ambiente(), "id");
			Usuario.setParametros(new Usuario(), "id", "nome", "permissao", "ativo");
			return ResponseEntity.ok(movimentacaoService.movimentar(itemId, ambienteId));
		} catch (EntityNotFound e) {
			return ResponseEntity.notFound().build();
		} catch (ConflictException e) {
			HashMap<String, String> map = new HashMap<>();
			map.put("x-motivo", "O item j� se encontra nesse ambiente");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
		}
	}
}
