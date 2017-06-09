/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Produtor;

import Interfaces.Configuracao;
import Interfaces.Produtor;
import java.io.FileNotFoundException;
import java.io.FileReader;
import static java.lang.Thread.sleep;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Leandro
 */
public class Main {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws java.util.concurrent.ExecutionException
     * @throws java.lang.InterruptedException
     * @throws java.rmi.RemoteException
     * @throws java.rmi.AlreadyBoundException
     */
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, FileNotFoundException, ExecutionException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        Scanner arquivo;
        
        // Configura propriedades de segurança
        configurarPropriedade("java.rmi.server.codebase", "file:" + System.getProperty("java.class.path").replace("\\", "/") + "/"); //só o windows precisa do replace

        // Instancia o objeto a ser distribuido
        ProdutorImpl localConj = new ProdutorImpl();
        ConfiguracaoImpl localConf = new ConfiguracaoImpl(60000); //60000ms (1 minuto) por padrão

        // Exporta o objeto a ser distribuido e retorna uma referência (stub)
        Produtor remotoConj = (Produtor) UnicastRemoteObject.exportObject(localConj, 0); //RemoteException
        Configuracao remotoConf = (Configuracao) UnicastRemoteObject.exportObject(localConf, 0); //RemoteException

        // Cria um rmiregistry (registro de serviços remotos) na porta 5500 da JVM e
        //retorna uma referência
        Registry r = LocateRegistry.createRegistry(5500); //RemoteException

        // Registra a ligação nomedoserviço-serviço no rmiregistry
        r.bind("Produtor", remotoConj); //RemoteException, AlreadyBoundException
        r.bind("Configuracao", remotoConf); //RemoteException, AlreadyBoundException

        System.out.println("CONCLUÍDO! Produtor pronto para produzir e disponibilizar sua produção.");

        arquivo = new Scanner(new FileReader(args[0])); //FileNotFoundException
        while (arquivo.hasNextLine()) {
            String linha = arquivo.nextLine();
            String colunas[] = linha.split(" ");
            if (Integer.parseInt(colunas[2]) > 0) { //quantidade de matrizes precisa ser maior que zero
                localConj.adicionaMatrizes(
                        (ArrayList<double[][]>) pool.submit(new LeitorDeArquivo(
                                colunas[0],
                                Integer.parseInt(colunas[1]),
                                Integer.parseInt(colunas[2]))).get()); //ExecutionException, InterruptedException
            }
            sleep(localConf.obtemIntervalo());
        }
        arquivo.close();
        pool.shutdown();

        System.out.println("CONCLUÍDO! Produtor concluiu a produção.");
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
