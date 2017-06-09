package ServidorDeExecucao;

import Interfaces.Execucao;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author Ingrid, Leandro
 */
public class Main {

	/**
     * @param args the command line arguments
     * @throws java.rmi.AlreadyBoundException
     * @throws java.rmi.RemoteException
     */
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        // Configura propriedades de segurança
        configurarPropriedade("java.rmi.server.useCodebaseOnly", "false"); //permite que o rmi local busque classes externas
        configurarPropriedade("java.security.policy", "file:seguranca.politica"); //permite que o consumidor insira tarefas externas
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        // Instancia o objeto a ser distribuido
        ExecucaoImpl localExec = new ExecucaoImpl(Integer.parseInt(args[0]));

        // Exporta o objeto a ser distribuido e obtém uma referência (stub)
        Execucao remotoExec = (Execucao) UnicastRemoteObject.exportObject(localExec, 0); //RemoteException

        // Cria um rmiregistry (registro de serviços remotos) na porta 5502 da JVM e
        //retorna uma referência
        Registry r = LocateRegistry.createRegistry(5502); //RemoteException

        // Registra a ligação nomedoserviço-serviço no rmiregistry
        r.bind("Execucao", remotoExec); //RemoteException, AlreadyBoundException

        System.out.println("CONCLUÍDO! Servidor pronto para receber tarefas.");

    }

    private static void configurarPropriedade(String chave, String valor) {
        //System.out.println(chave);
        //System.out.println(valor);

        System.setProperty(chave, valor);
        if (System.getProperty(chave) != null) {
            System.out.println("CONCLUÍDO! Propriedade "+chave+" configurada com sucesso.");
        } else {
            System.out.println("ERRO! Propriedade "+chave+" não foi configurada.");
            System.exit(1);
        }
    }
}
