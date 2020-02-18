# Sales Report #

## Aplicação - Overview ##

Esta aplicação foi desenvolvida para ser um mecanismo de processamento de arquivos com dados de vendas,clientes,vendedores e criação de relatórios baseados nestes dados. Foi desenvolvida na linguagem Java e utiliza [Spring Batch](https://spring.io/projects/spring-batch) para abstração e execução do processamento e [Spring Integration](https://spring.io/projects/spring-integration) para monitorar diretórios e iniciar job de processamento definido.

##  Arquitetura - Overview ##

  A arquitetura foi definida com base nos requisitos:
  
* **Processamento de arquivo e saída** Para este requisito optei pelo [Spring Batch](https://spring.io/projects/spring-batch) por conter uma implementação da [JSR-352](https://jcp.org/en/jsr/detail?id=352), que define uma abstração para o processamento, execução e agendamento de tarefas, além de prover todos os recursos do framework Spring, que é amplamente utilizado no mercado, contendo boa documentação e suporte ativo da comunidade.

* **Monitoramento de diretório** Foi utilizado o [Spring Integration](https://spring.io/projects/spring-integration), [InboundChannelAdapter](https://docs.spring.io/spring-integration/api/org/springframework/integration/annotation/InboundChannelAdapter.html) e [FileReadingMessageSource](https://docs.spring.io/spring-integration/api/org/springframework/integration/file/FileReadingMessageSource.html), para implementar a funcionalidade de monitoramento do diretório, disparando uma mensagem quando um arquivo chega, e transformando esta mensagem em um JobRequest, que posteriormente irá iniciar o processamento.

* **Escalabilidade** A aplicação pode ser publicada em containers [Docker](https://www.docker.com/), pois contém a descrição da construção do container no Dockerfile. Foi adotada uma estratégia de cache centralizado para garantir que mesmo com várias instâncias da aplicação escutando o mesmo diretório, cada arquivo seria processado apenas uma vez, a tecnologia de apoio para esta funcionalidade foi o [Redis](https://redis.io/), que fornece um mecanismo escalável e performático para definir e recuperar valores guardados em memória. A validação ocorre sempre que um arquivo .txt chega no diretório, a aplicação verifica no cache se alguma outra instância já está processando este arquivo, caso não esteja, o nome do arquivo é inserido no cache e removido um minuto após a finalização.

## Testes ##

  Todos os testes foram executados manualmente, escrevendo diferentes arquivos e verificando as saídas.

## Versionamento de código ##

  Para desenvolvimento de funcionalidades e geração de releases, utilizei o [Gitflow](https://github.com/nvie/gitflow), que é um modelo de controle de branches que suporta todo o ciclo de vida de uma entrega: desenvolvimento, homologação e release.

##  Build e deploy ##

* **Build** Este projeto contém um arquivo pom.xml, utilizado para descrever suas dependências e fazer o build via [Maven](https://maven.apache.org/).
  Existem 3 perfis configurados, para suportar diferentes ambientes de execução: **local**, para desenvolvimento local, **deploy-local** para execução do jar, e **deploy-container** para publicação em containers.
  
* **Deploy** Esta aplicação pode ser publicada em formato jar, para execução local e em um container, que contém o jar em um ambiente isolado de execução.

##  Instalação e execução ##

### Pre requisitos ###
   
   *  Java8+
   *  Docker

### Instalando

   * Baixe a imagem do Redis oficial para docker:  
     ```
     docker pull redis
     ```

   * Clone ou faça download do código desta aplicação e execute a partir da pasta raíz do projeto::
     ```
     mvnw install
     ```
   * Se estiver usando Linux:
     ```
     ./mvnw install
     ```

### Executando 
  - Inicie um container com a imagem do Redis, utilizando a porta **6379**:
     ```
     docker run -p 6379:6379 redis
     ``` 
  - Para executar localmente:
    - Execute o jar da aplicação que está no diretório target, substituindo **C:/desenv/input/** e **C:/desenv/output/** por seus diretórios de entrada e saida:(com a barra no final)
      ```
      java -jar -Dspring.profiles.active=deploy-local -DREDIS_HOST=localhost -DREDIS_PORT=6379 -DDATA_INPUT_FOLDER=C:/desenv/input/ -DDATA_OUTPUT_FOLDER=C:/desenv/output/ sales-report-1.0.0.jar  
      ```
  - Para executar em um container:
    - Na pasta raíz do projeto:  
      ```  
      docker build -t company/sales-projeto .  
      ```  
    - Rode a imagem em um container, substituindo **C:/desenv/input/** e **C:/desenv/output/** por seus diretórios de entrada e saida, e substituindo **192.168.0.8** pelo IP de rede da sua máquina:
      ```  
      docker run -v c:/desenv/input/:/tmp/input/ -v c:/desenv/output/:/tmp/output/ -e REDIS_PORT=6379 -e REDIS_HOST=192.168.0.8 -t company/sales-report  
      ```  

##  Testando ##

- Você pode iniciar várias instâncias utilizando o comando anterior e usar os arquivo contidos no diretório src/main/resouces/data/input como exemplo, ao copiar eles para o diretório de entrada definido nos passos anterioes, o processamento deverá ser iniciado automaticamente.

##  Limitações ##

Algumas limitações foram observadas, e não foram tratadas por falta de tempo:
  
  * Os arquivos de entrada são exclusivamente *.txt.
  * Os arquivos de saída são exclusivamente *.txt.
  * A aplicação não possui mecanismo de tratamento de falhas, caso algum arquivo inicie mas não consiga ser processado, será necessário retirar manualmente seu nome do cache para processá-lo novamente, é só acessar o redis-cli e utilizar:
  ```
  FLUSHALL
  ```

##  Melhorias ##

Algumas melhorias foram observadas, porém, não foram executadas por falta de tempo:
  
  * Mecanismo para tratamento de falhas. 
  * Utilização do [Docker Compose](https://docs.docker.com/compose/) para descrever e prover toda a infra necessária para a aplicação, dessa forma não seria necessário instalar e executar o [Redis](https://redis.io/) no host.
  * Criação de testes automatizados.
  * Separação de módulos consumidor-produtor para garantir a escalabilidade independente destas duas partes do software.
  * Documentação de código.
  * Utilização de regex para extração dos valores de vendas.
  * Avaliar e possivelmente implementar processamento paralelo.

##  Observações ##

  * Se o host for windows, as alterações no diretório de entrada não serão refletidas no container, dessa forma, para testar é necessário que os arquivo estejam no diretório antes da aplicação ser inicializada.

## Tecnologias ##

-  [Spring Batch](https://spring.io/projects/spring-batch)
-  [Spring Integration](https://spring.io/projects/spring-integration)
-  [FileReadingMessageSource](https://docs.spring.io/spring-integration/api/org/springframework/integration/file/FileReadingMessageSource.html)
-  [InboundChannelAdapter](https://docs.spring.io/spring-integration/api/org/springframework/integration/annotation/InboundChannelAdapter.html)
-  [Docker](https://www.docker.com/)
-  [Gitflow](https://github.com/nvie/gitflow)
-  [Maven](https://maven.apache.org/)
-  [Redis](https://redis.io/)