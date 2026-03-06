package br.social.impacthub.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record StandardResponse<T> (
        String status,
        T data,
        String message
){
    public static StandardResponse<Void> success(){
        return new StandardResponse<>("success", null, null);
    }

    public static <T> StandardResponse<T> success(T data){
        return new StandardResponse<T>("success", data, null);
    }

    public static StandardResponse<Void> fail(String message){
        return new StandardResponse<>("fail", null, message);
    }

    public static <T> StandardResponse<T> fail(T data){
        return new StandardResponse<>("fail", data, null);
    }

    public static StandardResponse<Void> error(String message){
        return new StandardResponse<>("error", null, message);
    }

    public static StandardResponse<Void> error(){
        return new StandardResponse<>("error", null, null);
    }
}
