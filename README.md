# Spring MVC Checkstyle Rules

## Rules Available
- `AlwaysRequireRequestParamWithAValueCheck` : this enforces when you define a `@RequestParam` annotation on a controller method you MUST provide a non-blank value/name 
- `AlwaysRequirePathVariablesWithAValueCheck` : this enforces when you define a `@PathVariable` annotation on a controller method you MUST provide a non-blank value

#### Note: all the above rules provide a property `additionalMessageOnViolation` that will allow you to provide a message that gives a better reason of why YOU require this rule and the said message will displayed when the rule fails.

## FAQ
- **How do I use these custom rules in my checkstyle configuration file?**
    - See one of the configuration files [here][1]


- **How do I integrate these custom rules into maven?**

 <project>
   <build>
     <plugins>
       <plugin>
         <groupId>org.apache.maven.plugins</groupId>
         <artifactId>maven-checkstyle-plugin</artifactId>
         <version>2.17</version>
         <dependencies>
           <dependency>
             <groupId>com.github.born2snipe</groupId>
             <artifactId>spring-mvc-checkstyle-rules</artifactId>
             <version>${latest.version}</version>
           </dependency>
         </dependencies>
       </plugin>
     </plugins>
   </build>
 </project>


[1]: https://github.com/born2snipe/spring-mvc-checkstyle-rules/blob/master/src/test/resources/config/spring/mvc