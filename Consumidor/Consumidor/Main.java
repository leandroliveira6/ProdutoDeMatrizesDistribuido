/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Consumidor;

import Interfaces.Execucao;
import Interfaces.Produtor;
import Interfaces.Configuracao;
import Interfaces.ConjuntoMatrizes;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import static java.lang.Thread.sleep;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;

/**
 *
 * @author Leandro
 */
public class Main {

	/**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.InterruptedException
     * @throws java.rmi.AlreadyBoundException
     */
    public static void main(String args[]) throws AlreadyBoundException, InterruptedException, IOException {
        MultiplicadorDeMatrizes mm;
        ConjuntoMatrizes conjMatr;
        Produtor remotoProdutor;
        Execucao remotoExecucao;

        // Configura propriedades de seguranÁa
        //para o servidor de execuÁ„o poder importar classes daqui
        configurarPropriedade("java.rmi.server.codebase", "file:" + System.getProperty("java.class.path").replace("\\", "/") + "/"); //sÛ o windows precisa do replace
        //para o consumidor poder importar classes de produtor
        configurarPropriedade("java.rmi.server.useCodebaseOnly", "false"); //permite que o rmi local busque classes externas
        configurarPropriedade("java.security.policy", "file:seguranca.politica"); //permite que o consumidor insira tarefas externas
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        
        // Instancia o objeto a ser distribuido
        ConfiguracaoImpl localConf = new ConfiguracaoImpl(500); //500ms por padr„o

        // Exporta o objeto a ser distribuido e retorna uma referencia (stub)
        Configuracao remotoConf = (Configuracao) UnicastRemoteObject.exportObject(localConf, 0); //RemoteException

        // Cria um rmiregistry (registro de serviÁos remotos) na porta 5501 da JVM e
        //retorna uma referÍncia
        Registry r = LocateRegistry.createRegistry(5501); //RemoteException

        // Registra a ligaÁ„o nomedoserÁo-serviÁo no rmiregistry
        r.bind("Configuracao", remotoConf); //RemoteException, AlreadyBoundException

        System.out.println("CONCLUÕDO! Consumidor concluiu a exportaÁ„o de seu objeto de configuraÁ„o.");

        // Obtendo os stubs necess·rios para o consumo
        remotoProdutor = (Produtor) obtemStub(args[0], Integer.parseInt(args[1]), "Produtor");
        remotoExecucao = (Execucao) obtemStub(args[2], Integer.parseInt(args[3]), "Execucao");

        // Inst‚ncia responsavel por dividir as matrizes em tarefas e envia-las para o servidor de execuÁ„o
        mm = new MultiplicadorDeMatrizes();
        try {
            while (true) {
                // ObtÈm uma cÛpia de um arraylist de matrizes (conjunto de matrizes)
            	conjMatr = remotoProdutor.obtemMatrizes(); //RemoteException (UnmarshalException)
                if (conjMatr == null) {
                    continue;
                } else {
                	mm.multiplicaMatrizes(remotoExecucao, conjMatr.obtemArrayList());
                }
                sleep(localConf.obtemIntervalo()); //InterruptedException
            }
        } catch (UnmarshalException ex) {
            System.out.println("AVISO! Consumidor perdeu alguma conex„o remota, encerrando a aplicaÁ„o...");
        }
        System.out.println("CONCLUÕçDO! Consumidor terminou.");
    }

    private static Remote obtemStub(String host, int porta, String nome) throws InterruptedException {
        Registry registro;
        Remote remoto;

        // Tenta por 5 vezes, no m·ximo, obter o stub
        for (int avisos = 0; avisos < 5; avisos++) {
            try {
                // ObtÈm uma referÍncia do rmiregistry em execuÁ„o no ip/porta especificado
                registro = LocateRegistry.getRegistry(host, porta); //RemoteException

                // Localiza o serviÁo pelo nome na referÍncia obtida
                remoto = registro.lookup(nome); //RemoteException (ConnectException), NotBoundException

                return remoto;

            } catch (RemoteException | NotBoundException ex) {
                switch (ex.getClass().getSimpleName()) {
                    case "ConnectException":
                        System.out.print("AVISO! Consumidor n„o pode se conectar, rmiregistry possivelmente n„o criado");
                        break;
                    case "NotBoundException":
                        System.out.print("AVISO! Consumidor n„o encontrou a referÍncia remota");
                        break;
                    default:
                        System.out.println("ERRO! Um erro desconhecido ocorreu, encerrando a aplicaÁ„o...");
                        System.exit(1);
                }
                System.out.print(" (" + nome + ", " + ex.getClass().getSimpleName() + ").");
                System.out.println(" Aguardando 10s atÈ a prÛxima tentativa...");
                sleep(10000); //InterruptedException
            }
        }
        System.out.println("ERRO! Algo de errado ocorreu ao tentar obter o stub "+nome+", encerrando a aplicaÁ„o...");
        System.exit(1);
        return null;
    }

    private static void configurarPropriedade(String chave, String valor) {
        //System.out.println(chave);
        //System.out.println(valor);

        System.setProperty(chave, valor);
        if (System.getProperty(chave) != null) {
            System.out.println("CONCLUÕDO! Propriedade "+chave+" configurada com sucesso.");
        } else {
            System.out.println("ERRO! Propriedade "+chave+" n„o foi configurada.");
            System.exit(1);
        }
    }
}
