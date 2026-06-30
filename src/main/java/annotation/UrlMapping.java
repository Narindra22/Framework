package annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) // Indique que l'annotation est visible pendant que Tomcat tourne
@Target(ElementType.METHOD)        // Indique que ce post-it se colle UNIQUEMENT sur des méthodes
public @interface UrlMapping {
    String value(); // Permet de passer l'URL en paramètre (ex: @UrlMapping("/emplist"))
    
}
