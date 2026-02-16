package br.com.ferdbgg;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        SerialPort[] serialPorts = SerialPort.getCommPorts();

        if (serialPorts.length == 0) {
            System.out.println("Nenhuma porta serial encontrada.");
            System.exit(1);
        }

        // Escolha a porta serial apropriada com base no seu Arduino
        SerialPort chosenPort = serialPorts[0]; // Altere se necessário

        if (chosenPort.openPort()) {
            System.out.println("Porta serial aberta com sucesso!");

            chosenPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 100, 0);

            InputStream in = chosenPort.getInputStream();

            try {
                int i = 0;
                while (true) {
                    // Aguarda até que haja dados disponíveis
                    while (in.available() == 0) {
                        Thread.sleep(20);
                    }

                    // Lê os dados disponíveis
                    byte[] buffer = new byte[in.available()];
                    in.read(buffer);

                    // Converte os bytes para string e imprime
                    String receivedData = new String(buffer);
                    System.out.println("Dado recebido" + i++ + ": " + receivedData);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            } finally {
                chosenPort.closePort();
                System.out.println("Porta serial fechada.");
            }
        } else {
            System.out.println("Falha ao abrir a porta serial.");
        }
    }

    public static void main2(String[] args) {
        // Detecta as portas seriais disponíveis
        SerialPort[] availablePorts = SerialPort.getCommPorts();
        System.out.println("Portas seriais disponíveis:");
        for (SerialPort port : availablePorts) {
            System.out.println(port.getSystemPortName());
        }

        // Escolha a porta correta
        SerialPort port = SerialPort.getCommPort("/dev/ttyUSB0"); // Alterar para a porta correta, ex: /dev/ttyACM0

        // Configura a porta serial
        port.setComPortParameters(9600, 8, 1, 0); // Configura para 9600 baud, 8 bits de dados, 1 bit de parada, sem
                                                  // paridade
        port.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 1000, 0);

        // Abre a porta serial
        if (port.openPort()) {
            System.out.println("Porta aberta com sucesso.");
        } else {
            System.out.println("Erro ao abrir a porta.");
            return;
        }

        // Ler dados da porta serial
        Scanner data = new Scanner(port.getInputStream());
        while (data.hasNextLine()) {
            String line = data.nextLine();
            System.out.println("Recebido: " + line);
        }

        // Fecha a porta quando terminar
        port.closePort();
        System.out.println("Porta fechada.");
    }
}
