<h1 align="center"> Cálculo de Coeficiente de Variação de Gols para Apostas Esportivas </h1>

## ✔️ Descrição
Trata-se de um bot que extrai os gols dos últimos jogos de algum time de futebol em um site e calcula o coeficiente de variação dos gols.

## ✔️ Funcionalidades
- `Pesquisa pelo time indicado;`
- `Extrai os gols dos últimos jogos de um determinado time;` 
- `Faz o cálculo da média de gols, do desvio padrão, da variância, do coeficiente de variação e da porcentagem de variação dos gols em relação à média;`

## ✔️ Tecnologias utilizadas
- ``Java 11``
- ``Spring Boot``
- ``Thymeleaf``
- ``Bootstrap``
- ``Selenium WebDriver``
- ``Eclipse IDE``
- ``JUnit``
- ``Maven`` 

## ✔️ Execução
Após ter feito o git clone do projeto, abra ele com sua IDE e rode o "ApplicationAposta.java". Após isso deve-se abrir seu navegador e acessar a aplicação pelo http://localhost:8080/add . Digite o nome do time e clique em "CADASTRAR". Irá abrir uma tela do Firefox* e realizar a busca no site. Após a tela do Firefox ser fechada, você irá ser direcionado para http://localhost:8080/resposta e observará os resultados podendo acrescentar uma nova busca ou limpando o resultado de pesquisa para uma nova consulta.  Por fim é só rodar a classe "Time", ele irá abrir o navegador e procurar no site pelo time idicado como String na classe "Time", caso queira poderá alterar o valor da String com o time desejado. No repositório contém as imagens das fórmmulas utilizadas e do site.

*(exite a possibilidade de usar o Chrome como navegador e pra isso terá que alterar o driver contido em driver/chromedriver com o compatível com a versão do seu navegador, e, também, dar permissão para o arquivo chromedriver atualizado)
