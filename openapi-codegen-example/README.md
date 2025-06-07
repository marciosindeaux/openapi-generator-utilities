# Os basicos do Springdoc Usando Openapi Generator

O Projeto contido nesta pasta é um projeto simples. Ele visa mostrar como é montar um microsserviço com openapi-generator do zero. 
Ele foi gerado a partir do site do [Spring Initializer](https://start.spring.io/)
___
## Gerando o projeto: 

Como já dito anteriormente, esse projeto foi gerado no spring initializer, e caso seja do seu interesse gerar um do zero, gere com as seguintes tecnologias: 
 * `Kotlin`
 * `Gradle DSL (para kotin)` 
 * `Spring Boot 3.5.0`
 * `Java 17`
 * `Spring Web (spring-boot-starter-web)`

Dessa forma o projeto gerado vai se parecer com este aqui e terá as exatas tecnologias que estão aqui presentes.

Note que ao gerar o projeto seu arquivo de build deve ter `.kts` no final para que o seu projeto esteja com `Gradle Kotlin DSL`. Caso o seu projeto não esteja com isso, a sintaxe que será usada nas configurações do arquivo de build será a sintaxe do Groovy.

Aqui neste artigo não cobriremos Gradle para Groovy, mas com as ferramentas atuais de busca os arquivos podem ser facilmente convertidos e adaptados para sua necessidade, caso deseje isso.

___
## Porque usar o Openapi generator.
Se você é mais novo ou nunca mexeu em projetos legados, não sabe como era a dor de cabeça de criar ou manter uma documentação de API para um projeto novo ou ja existe.

Em ambientes de fábricas de software onde a rotatividade é grande e por diversas vezes a qualidade dos profissionais pode ser... questionavel, por asim se dizer, nem todos se lembram de documentar os seus endpoints ou os seus objetos de requisição e resposta (as vezes nem ha tempo habil para isso).

Por conta das bibliotecas de versões mais antigas (até mesmo na springfox, que hoje é considerada legado), criar uma documentação era sempre uma dor de cabeça, e manter ela era mais ainda. 

Eram annotations e mais annotations para conseguir documentar um simples endpoint. Sem contar na quantidade infindada de erros que poderiam acontecer com a build do projeto por conta de versões diferentes de libs que conflitavam internamente.

Pensando nisso a biblioteca do openapi-generator foi criada. Unificando as libs para evitar conflitos e simplificando as coisas de forma que pudesse ser integrado a maioria dos ambientes em pouco tempo. 
Isso porque ela é facilmente integravel e manutenível, já que tudo (a assinatura da sua api, os objetos de request e response, as documentações) vai ser gerado a partir de um arquivo yaml que também será usado para fornecer a documentação e o swagger da api.

### Vantagens do uso do openapi-generator
 * Geração de swagger automatico sem configurações complexas
 * Beans relativos à entrada e saida de dados gerados automaticamente
 * Annotations de documentações gerados automaticamente
 * Beans gerados para os seus Controllers com todas as documentações do openapi 
 * Centralização da documentação 
 * Facil Integração e Manutenção com código já existente

