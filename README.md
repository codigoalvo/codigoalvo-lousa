focaand-lousa
=============

focaAnd - Lousa - App for Android



Sobre focaAnd:
focaAnd, um grupo criado com o propósito de desenvolver o TCC/IC (Trabalho de Colclusão de Curso / Iniciação Científica) do curso de Bacharelado em Ciência da Computação (Formandos de 2014). Composto pelos alunos: Cássio Reinaldo Amaral, Eduardo Rodrigues Sousa e Vitor Rossetto e orientado pelo professor Wonder Alexandre (Professor de Computação Gráfica)

Sobre Lousa:
A idéia do app lousa surgiu da necessidade dos componentes do grupo. Pois desde o início do curso arquivamos a matéria passada pelos professores em sala de aula através de fotos tiradas do quadro verde de giz (lousa) com nossos celulares. Estas fotos além de ocupar muito espaço, não são conveniêntes a serem impressas ou visualizadas em tablets por exemplo. Aí surgiu a idéia de criarmos um aplicativo para aplicar filtros nas fotos tiradas, invertendo as cores e transformando a imagem final em algo próximo de um texto em tons de cinza em um fundo branco. Como nosso projeto caminhou para uma Iniciação Científica e não um simples TCC, fomos orientados pelo professor à direcionar nosso app como uma aplicação prática de um projeto de segmentação de imagens de outra iniciação científica. A segmentação de imagem teria o papel de eliminar das fotos partes não interessantes, como parede, moldura da lousa, etc, através de seleção de background/foreground desenhando imagens marcadoras sobre a foto.




Email inicial da iniciação científica:

A proposta do nosso projeto, inicialmente concebido para ser desenvolvido como TCC, porém sugerido para ser trabalhado como Iniciação Científica (IC) pelo professor Wonder é o seguinte:

Desenvolvimento de um app para android para captura, processamento e armazenamento/organização de fotos tiradas de quadro negro/verde.

A idéia surgiu a partir da necessidade pessoal observada no docorrer dos semestres de curso na Uninove, onde temos o hábito de fotografar o quadro com as anotações/matéria passada pelo professor.

O problema original é que as imagens armazenadas não são adequadas para serem impressas caso necessário, pois gastam muita tinta pelo motivo do fundo verde ou preto serem a maior parte da imagem. Essas imagens também ocupam muito espaço em disco para serem armazenadas por se tratar de imagens coloridas e de alta definição.

A idéia principal e também a maior dificuldade do projeto proposto é encontrar e aplicar filtros na foto tirada deixando esta com fundo branco e os traçados de texto e desenhos em giz em preto/tons de cinza. Os filtros devem ser capazes de identificar as variações de tons de verde/preto do quadro e também suas imperfeições e ruídos, eliminando estes porém sem eliminar acidentalmente o conteúdo principal (textos e desenhos).

Para que a idéia principal, inicialmente concebida para o TCC seja desenvolvida como IC deveremos fazer uso de uma Biblioteca/API desenvolvida por um grupo também orientado pelo professor Wonder. Esta API possui recursos para identificar de forma simplificada o que é o conteúdo principal e o que deve ser eliminado da imagem, e será utilizada para separar o quadro negro/verde do fundo que o cerca (parede, moldura, etc...), trabalho que sem esta ferramenta deveria ser feito manualmente pelo usuário do APP.

Na medida do possível levando em consideração tempo e nosso conhecimento limitado, temos a intenção de desenvolver os métodos para a organização e talvez armazenamento em nuvem das fotos processadas, facilitando assim recuperação para estudo posterior.

Att,

Cássio Reinaldo Amaral
São Paulo, 28 de Outubro de 2013






Bom dia professor, tudo bem?
Sei que estamos "sumidos", mas lembra-se da gente?

Grupo de Ciência da computação que está fazendo Iniciação Científica de um app em android para aplicar filtros em foto tirada de quadro negro/verde para aarquivar como notas, utilizando-se da biblioteca fornecida criada pelo outro grupo, que faz segmentação da imagem, a qual será usada no nosso app para eliminar tudo aquilo que não é o quadro da imagem..

Pois bem,
O projeto caminhou pouco durante as férias (quase nada na verdade), mas assim que as aulas retornaram demos uma agilizada e as coisas estão saindo.

Já conseguimos desenvolver a activity que captura a foto, a activity que desenha sobre a foto para montar o marcador que é passado para a biblioteca, já conseguimos utilizar a biblioteca e obter a imagem transformada por ela. (O primeiro resultado com sucesso foi ontem, então ainda teremos alguns ajustes para fazer.

As principais dificuldades encontradas até agora e as soluções dadas para elas foram:
1) Ao transferir a imagem capturada pela câmera de uma activity há uma enorme lentidão. -> A solução foi salvar a imagem em disco e transferir o caminho do arquivo salvo de uma activity para outra. (Aparentemente essa solução é muito usada em apps para android).

2) O "desenhar" sobre a imagem ainda não está preciso, visto que temos problemas de resoluções de telas de aparelhos diferentes, e resoluções de imagens diferentes. Ao deslizar sobre uma tela 1920x1080 em uma imagem 1024x768 existem áreas da tela que ficam "em branco", não ocupadas pela imagem, e isso afeta o cálculo de proporção para converter o pixel capturado do toque na tela para o pixel a ser pintado na imagem. Já fiz diversos ajustes, chegando a algo aceitável pelo menos por enquanto, mas poderemos aperfeiçoar mais no futuro.

3) A utilização da do projeto fornecido "tcc-seg-ift" a princípio não foi totalmente compatível com o android, basicamente por utilizar classes do AWT/Swing, como "Color", "Point", etc. Tivemos que alterar o código fonte do projeto, removendo essas classes e criando novas classes para fazer as funções dessas classes, removendo assim a compatibilidade. Posteriormente vou lhe passar a listagem das classes removidas e das classes criadas, assim, caso possa ser de interesse seu ou do grupo de alterar permanentemente o projeto para utilizar estas classes.

4) A utilização da lib gerada por este projeto de segmentação fica extremamente lenta no android. Pela capacidade pequena de recursos (memória e processamento) nos dispositivos android, o processamento da segmentação é lento e custoso para os aparelhos. Mas isso não é um impeditivo, apenas algo que não está legal, mas que está além das nossas capacidades atuais e do nosso escopo de investigar para resolver. Seria interessante ver com o grupo que desenvolveu, se há utilização de técnicas como "recursividade" por exemplo no projeto, pois isso é algo extremamente custoso em termos de memória. Se houver e for possível adaptar para outra solução, acredito que o desempenho poderá melhorar muito. Futuramente podemos detalhar melhor o tempo de processamento, talvez até por partes.

5) Ainda não aplicamos na prática o "crop" da imagem capturada/importada a partir da segmentação (visto que o primeiro resultado com sucesso da segmentação foi obtido ontem a noite), mas em breve estaremos aplicando este "crop" na imagem.

6) Ainda não estamos aplicando os filtros de processamento da imagem para transformação da foto do quadro em nota para arquivo/estudo que é um dos pontos principais do projeto, mas já está sendo feita uma pesquisa de quais filtros serão necessários, sua ordem de aplicação e o algorítimo e/ou codigo em java para utilização no projeto.


Considerações finais:

Eu acredito que dentro de mais umas 2 semanas, talvez menos, teremos uma versão "beta" do aplicativo para podermos apresentar. Assim que tivermos essa versão beta agendaremos uma data para apresentarmos o projeto.

Nosso objetivo era ter essa versão beta até o final de março, mas estamos contando com a possibilidade de um pequeno atraso de até uma semana.
Nossa idéia é de entregarmos a versão final para concluirmos o processo de iniciação científica até o final de abril, e a partir de agora tentarei fazer relatórios mais frequentes.

Os commits/código fonte do projeto estão disponíveis no bitbucket através de git, por onde pode ser possível ter acesso ao projeto todo e/ou ao histórico de commits do projeto.

Ainda esta semana tentarei atualizar o Trello com os bugs pendentes de solução e as próximas etapas do projeto.

Peço por gentileza conformação de recebimento deste email e assim que possível ficariamos gratos pelas suas observações gerais.

Att,

Cássio Reinaldo Amaral
19 de Março de 2014
Ciência da Computação - 7º Semestre / 2014 - Noturno - Uninove Memorial






Bom dia professor.
Para utilizarmos o projeto "TCC-SEG-IFT" que nos foi passado pelo sr. no nosso projeto, tivemso que fazer algumas adaptações para tornar compatível com a API do android.
Segue relatório enviado pelo Vitor do nosso grupo que fez a adaptação. Vou incorporar essas informações também no email que estou preparando que vou lhe enviar ainda hoje com as dificuldades encontradas e as medidas tomadas durante o decorrer do projeto.
No próximo email também enviarei as duas classes criadas mencionadas pelo Vitor, e o projeto alterado para utilizar essas classes assim como o jar gerado a partir desse projeto que é o que utilizamos no nosso projeto no android.

Att,

Cássio Reinaldo Amaral
15 de Abril de 2014





Professor, bom dia.
Conforme combinado, segue status report com as principais dificuldades encontradas e as medidas tomadas no projeto de iniciação científica.

Dificuldades encontradas e medidas tomadas:

1) O projeto tcc-seg-ift que nos foi entregue para ser utilizado em nosso projeto não era compatível com a API do android.
* Adaptamos o projeto, removendo as classes que eram do AWT (que não são compatíveis com o android) e criando classes "pojo" para substituí-las. AS classes do AWT removidas foram "Color" e "Point" e os métodos que utilizavam BufferedImage foram também removidos. As clases que utilizavam essas classes que foram adaptadas foram: [1] ImageBuilder, [2] BinaryImage, [3] FloatImage, [4] GrayScaleImage, [5] IImage, [6] IRGBImage, [7] RGBImage

2) Ao processar a segmentação utilizando a o projeto tcc-seg-ift, o processo leva um tempo considerávelmente grande demais, devido aos recursos limitados de dispositivos móveis como principalmente memória e processador.
* Não encontramos ainda solução definitiva para este problema, visto que são pertinentes à forma como o projeto tcc-seg-ift foi desenvolvido (Desenvolvido pensando em aplicações desktop, onde memória e processador são recursos abundantes).
O que temos são sugestões de possiveis pontos à serem analisados no tcc-seg-ift, como: * Verificar se utiliza métodos recursivos, uma vez que recursividade é o grande vilão de consumo de memória. * Verificar a possibilidade do tcc-seg-ift ter métodos que recebam um parâmetro de resolução máxima de processamento, assim, por exemplo, ao utilizar uma imagem de 1280x720 e passando o parametro como 640, tanto a imagem original quanto a imagem marcador seriam redimencionados para 640x360, processadas, aplicado a segmentação na resolução original e devolvida a imagem original processada. Chegamos a essa conclusão à partir da solução paleativa que tomamos no nosso projeto android, colocando uma opção de resolução máxima da foto na tela de configurações. O que fazemos é o redimensionamento como o sugerido antes de enviarmos para o tcc-seg-ift processar. O problema é que a imagem final após o processamento é na resolução reduzida.

3) Tivemos dificuldade com a utilização de bitmaps no android.
Aparentemente bitmaps utilizam muita memória e frequentemente tinhamos problemas de "out of memory".
* Seguindo recomendações de fóruns na internet, utilizamos o recycle() e null nos objetos bitmaps ao terminar de utilizá-los, seguido de um System.gc() que força o garbage collector a liberar memória.

4) Tivemos diversos problemas com a activity personalizada de obtenção de foto da câmera do android.
Desde problemas de rotação, à tamanho da imagem, foco, entre outros.
* Utilizamos a API que permite o usuário selecionar o "app" para obtenção da foto. Permitindo utilizar o app default do android para tirar a foto.

5) Com o 'intent' default do android de camera, alguns modelos de celulares travavam o app ao tirar foto.
* Realizamos algumas adaptações seguindo os padrões informados no site de documentação / apoio ao desenvolvedor da própria google (documentação do android) para obtenção das fotos.
Aperentemente resolveu o problema na maioria dos modelos de dispositivos.
Colocamos na tela de configuração uma opção para duas formas diferentes de aquisição de foto pelo intent default do android.

6) Não conseguimos obter uma qualidade esperada da imagem pós-processada usando treshold e resultado final em preto e branco.
* Colocamos na tela de configurações do app uma opção permitindo selecionar preto e branco ou não.
O grayscale gera um resultado um pouco mais "claro" porém melhor em nossa opnião.

7) Botões atarpalhavam ao desenhar na tela para marcar as áreas de segmentação.
* Colocamos na tela de configurações uma opção para ocultar os botões, utilizando as funcionalidades dos botões através de menu de contexto.

8) Cálculos de proporções de imagem a ser segmentada e imagem de marcador de segmentação ao desenhar para marcar as áreas de segmentação não funcionavam como esperado.
* Resolvemos o problema fazendo um "strech" (esticando) da imagem para o tamanho máximo da tela do dispositivo, respeitando a orientação (retrato/paisagem) atual do dispositivo.

9) Ao rotacionar o dispositivo o "onCreate" da activity era chamado novamente causando diversos erros graves.
* Esse é um comportamento padrão do android. O que fizemos foi colocar "Dialog de aguarde" enquanto executamos processamentos e travamos a rotação da tela durante o processamento, devolvendo a funcionalidade de rotação após fechar o dialog.


---------------------------------------------------------------------------------------


Anexo seguem as classes java Color e Point criadas para o projeto tcc-seg-ift, assim como o projeto tcc-seg-ift alterado e sua lib "jar" gerada para utilização no projeto lousa.

Também em anexo versão beta (build Tue, Apr 15 2014 09:25:35) do apk do lousa para instalação em dispositivos android.

Cássio Reinaldo Amaral
15 de Abril de 2014
