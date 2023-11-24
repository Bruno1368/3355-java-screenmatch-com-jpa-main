package br.com.alura.screenmatch.model;

public enum Categoria {

    COMEDIA("Comedy"),
    ROMANCE("Romance"),
    ACAO("Action"),
    DRAMA("Drama"),
    CRIME("Crime");

    private String categoriaOmdb;

    Categoria(String categoriaOmdb){
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text){
        for (Categoria categoria : Categoria.values()) {
            if(categoria.categoriaOmdb.equals(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nnehuma categoria encontrada para esse filme");
    }
}
