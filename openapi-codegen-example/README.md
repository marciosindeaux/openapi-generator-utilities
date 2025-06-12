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

De qualquer forma, em ambos os casos adicione o seguinte plugin para seu arquivo de build: 
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

### 3) Configurando a geração de codigo na sua build

Essa talvez seja a parte mais importante e parte que muitos erram. Configurar o `build.gradle.kts` não é um bixo de 7 cabeças, mas muitos desenvolvedores ignoram que podem mexer em seus arquivos de builds, personalizar pipelines ou criar novas rotinas para serem executadas durante a build. Muitos SEQUER SABEM que podem fazer isso, seja no maven ou no gradle. 

Independente do que você esteja usando para fazer a build do seu projeto (Maven, Gradle ou SBT), é importante que você conheça as opções e as limitações que a sua ferramenta tem.

Dito isso vamos aos passos: 
 * No seu arquivo `build.gradle.kts` coloque a seguinte task em seu arquivo : 
```kotlin
openApiGenerate {
	generatorName.set("kotlin-spring")
	inputSpec.set("$rootDir/src/main/resources/static/api-docs.yaml")
	outputDir.set(layout.buildDirectory.dir("generated/openapi").get().asFile.absolutePath)
	modelNameSuffix.set("ExternalModel")
	apiPackage.set("com.sindeaux.openapi_codegen_example.application.web.apis")
	modelPackage.set("com.sindeaux.openapi_codegen_example.application.web.models")
	configOptions.set(
		mapOf(
			"dateLibrary" to "java8",
			"gradleBuildFile" to "false",
			"interfaceOnly" to "true",
			"openapiNullable" to "true",
			"useTags" to "true",
			"useBeanValidation" to "true",
			"useSpringBoot3" to "true"
		)
	)
}
```

 * Logo abaixo adicione no mesmo arquivo a task de sourceSets : 
```kotlin
sourceSets {
	getByName("main") {
		kotlin {
			srcDir(layout.buildDirectory.dir("generated/openapi/src/main/kotlin").get().asFile.absolutePath)
		}
	}
}
```
 * E por último verifique se existe uma task chamda `compileKotlin`. Caso ela não exista crie, caso ela exista apenas adicione o dependsOn:
```kotlin
tasks.compileKotlin {
	dependsOn("openApiGenerate")
}
```

### 4) Configurando a aplicação para exibir a documentação do springdoc

Nesse ponto ja estamos praticamente nos finalments das configurações. Nesse ponto ja temos codigo gerado pelo arquivo da especificação, mas queremos que o swagger apareça certo ?

Para isso acontecer é bem simples. Vá no seu application.yaml e configure o springdoc da seguinte maneira: 
```yaml
springdoc:
  api-doc:
    enabled: true
  swagger-ui:
    enabled: true
    path: /docs
    url: /api-docs.yaml
```
O Springdoc tem uma série de configurações que conseguem te dar uma extensa lista de libedades para as suas necessidades. Não vamos entrar a fundo neste topico ainda, com excessão de dois itens: 
 * `springdoc.swagger-ui.path` :

        Variavel resonsavel por indicar qual URL da sua api que vai redirecionar para o swagger. 
        Cada caso é um caso , mas existem situações em que é necessário que se modifique o valor.
        Por exemplp para que se adapte a alugum path que sua segurança permita ser acessado.
 * `springdoc.swagger-ui.url`:

        Variavel responsavel por dizer para o springdoc onde seu arquivo de especificação openapi está guardado no sistema.
        Ele olha dentro da pasta src/main/resources/static, ja que essa é a pasta que a aplicação vai sempre servir arquivos estaticos.

#### Observação Importante:

Em alguns ambientes é preferivel que não se sirvam arquivos estaticos, mesmo que eles estejam protegidos. Neste caso voce pode configurar a aplicação para que escaneie os dados e anotações apartir do codigo gerado pelo OpenApiGenerator em um path especifico. Para casos assim seu `application.yaml` ficará assim :
```yaml
springdoc:
  packages-to-scan:
    - com.sindeaux.openapi_codegen_example.application.web
  swagger-ui:
    path: /docs
```
<b>Note que este pacote é o mesmo que compõe `apiPackage` e `modelPackage`. É importante que os pacotes de cada um dos dois esteja na lista em `packages-to-scan`</b>


Dessa forma o springdoc vai escanear esse pacote atrás das annotations do openapi,e  gerar o swagger apartir do codigo gerado.

### 5) Implementado nos seus controllers

Essas é a parte simples. Se voce configurou certo tudo até aqui, basta rodar um `gradle clean build` no seu console e verificar que na pasta `build/generated/openapi/src/main/kotlin` tem diversas classes geradas a depender de como ficou o seu arquivo `api-docs.yaml`. Basta ir para a pasta que voce configurou o `apiPackage` e verificar que lá tem as interfaces dos seus controllers.

No meu caso a classe `ExampleEndpointApi` foi gerada com o seguinte conteudo:

```kotlin
@RestController
@Validated
interface ExampleEndpointApi {

    @Operation(
        tags = ["ExampleEndpoint",],
        summary = "Endpoint Example to generate code",
        operationId = "exampleMethod",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "Success")
        ]
    )
    @RequestMapping(
            method = [RequestMethod.GET],
            value = ["/example"]
    )
    fun exampleMethod(): ResponseEntity<Unit> {
        return ResponseEntity(HttpStatus.NOT_IMPLEMENTED)
    }
}
```

Veja que nela contem inclusive as annotations do Spring com `@RestController` e `@RequestMapping`.

Crie uma classe Controller na sua aplicação. No meu caso eu criei o `ExampleEndpointController` e implemente essa interface gerada: 
```kotlin
class ExampleEndpointController() : ExampleEndpointApi {

    override fun exampleMethod() : ResponseEntity<Unit> {
        print("You have a request for your endpoint in method exampleMethod()")
        return ResponseEntity.ok().build()
    }
}
```

Pronto, apartir daqui você agora tem uma forma eficiente não só de documentar a suas apis, mas também de otimizar o seu tempo, te poupando de escrever dezenas de anotações especificas para documentar um único Endpoint ou Controller.

Espero que tenha gostado e até a próxima