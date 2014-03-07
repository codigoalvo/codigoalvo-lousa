package tcc.datastruct;

import java.util.LinkedList;

/**
/**
 * Projeto de implementacao de Estrutura de dados
 * @author Wonder Alexandre Luz Alves
 * 
 * <b>Interface Stack </b><br>
 */


public class Stack<E> {
    /**
     * lista encadeadas
     */
    protected LinkedList<E> list;

    /**
     * construtor da classe
     */
    public Stack() {
        list = new LinkedList<E>();
    }

    /**
     * metodo que esvazia a pilha
     */
    public void clear() {
        list.clear();
    }

    /**
     * metodo que insere um objeto na pilha
     * 
     * @param object
     */
    public void push(E object) {
        list.add(object);
    }

    /**
     * retorna o objeto retirado da pilha
     * 
     * @return
     */
    public E pop() {
        return list.removeFirst();
    }

    /**
     * metodo que retorna o objeto do topo da pilha
     * 
     * @return objeto do topo da pilha
     */
    public E getTop() {
        return list.getFirst();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }

}
