package br.com.leandro.todolist.utils;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class Utils {

  // copiar tudo que não for nulo.
  public static void copyNonNullProperties(Object source, Object target) {
    // copiar as propriedades de um objeto para outro objeto
    BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
  }

  // pegar o nome de todas as propriedades que estão nulas
  public static String[] getNullPropertyNames(Object source) {
    // BeanWrapper é uma interface que fornece uma forma de acessar as propriedades
    // de um objeto java e o BeanWrapperImpl é a implantacao dessa interface;
    final BeanWrapper src = new BeanWrapperImpl(source);
    System.out.println("BEANWRAPPER: " + src);

    // pegando as propriedades do objeto e retornar um array de propertyDescriptor
    PropertyDescriptor[] pds = src.getPropertyDescriptors();

    // criando um conjunto com as propriedades que eu tenho de valores nulos
    // A estrutura HashSet implemementa a interface Set, Estruturas de dado do tipo
    // “Set” são conhecidas por aceitar apenas valores únicos, ou seja, qualquer
    // valor duplicado inserido em um “Set” será automaticamente excluído
    Set<String> emptyNames = new HashSet<>();

    // iterando o array de propriedades
    for (PropertyDescriptor pd : pds) {
      // obtendo o valor da propriedade atual
      // pd.getName() - nome da propriedade
      Object srcValue = src.getPropertyValue(pd.getName());

      // se o valor da propriedade for null vou add o nome da propriedade no conjunto
      // empty names Set

      if (srcValue == null) {
        emptyNames.add(pd.getName());
      }
    }

    // criando um array de strings com o tamanho do emptyNames
    String[] result = new String[emptyNames.size()];

    // converter o emptyNames em array
    return emptyNames.toArray(result);
  }
}
