## Build Pipeline Refactoring Kata

Repositório original:  
https://github.com/emilybache/BuildPipeline-Refactoring-Kata

### Projeto Original

O projeto original simula uma pipeline de build e deploy, responsável por verificar a existência de testes, executá-los, realizar o deploy e enviar um resumo por e-mail com o resultado, além de registrar logs de sucesso e falha. Toda essa lógica estava concentrada em uma única classe `Pipeline`, o que aumentava a complexidade e dificultava a manutenção.

### Objetivo da Refatoração

A refatoração teve como objetivo melhorar a estrutura do código sem alterar o comportamento original, além de implementar a nova funcionalidade proposta no kata:

- Realizar deploy em _staging_
- Execução de _smoke tests_
- Deploy em produção apenas se os _smoke tests_ passarem
- Falha da pipeline caso não existam _smoke tests_
- Enviar mensagens em logs e emails

### Melhorias Realizadas

- Criação de testes automatizados para proteger o comportamento existente
- Redução da complexidade do método `run()` por meio de extração de métodos menores
- Melhoria na legibilidade e organização da lógica da pipeline
- Separação de responsabilidades em classes específicas
