package br.com.alura.screenmatch.model;

public enum Categoria {

    COMEDIA("Comedy", "Comédia"),
    ROMANCE("Romance", "Romance"),
    ACAO("Action", "Ação"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime");

    private String categoriaOmdb;

    private String categoriaPortugues;

    Categoria(String categoriaOmdb, String categoriaPortugues){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }

    public static Categoria fromString(String text){
        for (Categoria categoria : Categoria.values()) {
            if(categoria.categoriaOmdb.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para esse filme");
    }
    public static Categoria fromPortugues(String text){
        for (Categoria categoria : Categoria.values()) {
            if(categoria.categoriaPortugues.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para esse filme");
    }

}
