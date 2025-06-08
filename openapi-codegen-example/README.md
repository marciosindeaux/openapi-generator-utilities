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

Em ambientes de fábricas de software onde a rotatividade é grande e por diversas vezes a qualidade dos profissionais pode ser... questionavel, por assim se dizer, nem todos se lembram de documentar os seus endpoints ou os seus objetos de requisição e resposta (as vezes nem ha tempo habil para isso).

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

___

## Usando o openapi-generator

Se você chegou até aqui, está na hora de pegar o seu codigo e vamos implementar passo a passo como fazer a integração desta lib no seu projeto.
### 1) Inserido dependencias e plugins

Ja que estamos aqui, vamos começar pelo começo. A pergunta é: qual lib do spring web você está usando? 

`spring-boot-starter-web` ou `spring-boot-starter-webflux`? Saiba que o openapi-generator funciona em ambas, mas a lib do springdoc muda. 
#### Instalando a versão correta para a sua aplicação você já garante a integração entre o projeto spring e o swagger (como diz a documentação [aqui](https://springdoc.org/#kotlin-support)). 

 * Caso esteja usando `spring-boot-starter-web` inclua as seguintes dependencias: 
```kotlin
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.8.8")
```
 * Caso esteja usando o `spring-boot-starter-webflux` inclua as seguintes dependencias: 
```kotlin
	implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.8.8")
	implementation("org.springdoc:springdoc-openapi-starter-webflux-api:2.8.8")
```

De qualquer forma, em ambos os casos adicione o seguinte plugin para seu arquivo de build : 
```kotlin
	id("org.openapi.generator") version "7.13.0"
```
 Depois que tudo estiver inserido, e seu projeto estiver buildado. Apartir deste ponto podemos confiugrar o springdoc do projeto (o que vamos fazer por último).
 
### 2) Criando um arquivo da especificação openapi

Essa é uma atividade bem simples pra ser sincero, mas de extrema importancia. Vamos criar o arquivo que vai ser responsavel por gerar e documentar as interfaces de API e as classes de request e response. Este arquivo também vai ser o que será mantido para expansao da documentação e das funcionalidades.

É de suma importancia que todos os desenvolvedores envolvidos no projeto tenham em mente que fazer e manter este arquivo é muito mais rapido para eles até para construir novas funcionalidades. E mesmo que eles não entendam a importancia, bom, voce sempre pode usar o [ArchUnit](https://www.archunit.org/) para fazer dessa cultura uma norma de arquitetura.

#### Dada as devidas observações, vamos para o passo a passo: 

 * Na sua pasta resources crie uma pasta chamada `static`(se ela já não estiver criada). Dentro desta pasta ficam todos os arquivos estaticos que o serviço vai servir. .
 * Crie um arquivo `yaml` ou `json` com o nome que quiser, aqui eu chamei de `api-docs.yaml`, mas voce pode chamar do que quiser desde que tenha uma das extensões. Aqui nós vamos abordar o arquivo `.yaml`.
 * Dentro deste arquivo, coloque a especificação da documentação da sua api.  Para exemplificar temos o arquivo deste projeto com o seguinte conteudo:

```yaml
openapi: 3.0.0
info:
  version: 1.0.0
  title: OpenAPI Codegen Example
  description: This is an example to other peoples
servers:
  - url: 'http://localhost:7000'
paths:
  /example:
    get:
      summary: Endpoint Example to generate code
      operationId: exampleMethod
      tags:
        - ExampleEndpoint
      responses:
        '200':
          description: Success
```

Vamos tratar sobre os detalhes do que pode ou não pode ser feito neste corpo em outro artigo, mas você pode conferir [aqui](https://swagger.io/specification/) a documentação de como criar um arquivo de especificação openapi para sua api apartir da sua necessidade.
