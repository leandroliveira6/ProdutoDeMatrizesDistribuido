# Produto de Matrizes Distribuido

Usando RMI

| Classe | Args | Descrição |
| ------ | ------ | ------ |
| Produtor.Main | CaminhoDasMatrizes.txt | Endereço para arquivo contendo uma lista de endereços de arquivos que contém matrizes |
| ServidorDeExecucao.Main | 5 | Número de threads desejado |
| Consumidor.Main | 127.0.0.1 5500 127.0.0.1 5502 | IP/Porta do produtor e IP/Porta do servidor de execução |
| Configurador.Main | 127.0.0.1 5500 127.0.0.1 5501 | IP/Porta do produtor e IP/Porta do consumidor |
