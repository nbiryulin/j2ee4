package utils;

import exceptions.DuplicateModelNameException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import model.Car;
import model.Moto;
import model.Transport;
import model.TransportType;

public class Utils {

    private static final Charset charset = StandardCharsets.UTF_8;

    public static double countAverage(Transport transport) {
        double[] prices = transport.getPrices();
        return Arrays.stream(prices).sum() / prices.length;
    }

    public static double countAverage(Transport[] transports) {
        OptionalDouble prices = Arrays
            .stream(transports)
            .flatMapToDouble(v -> Arrays.stream(v.getPrices()))
            .average();
        return prices.orElse(0);
    }

    public static void printNames(Transport transport) {
        System.out.println(Arrays.toString(transport.getModelNames()));
    }

    public static void printPrices(Transport transport) {
        System.out.println(Arrays.toString(transport.getPrices()));
    }

    public static void outputTransport(Transport transport, OutputStream out) { //DataOutputStream
        try (DataOutputStream stream = new DataOutputStream(out)) {

            String type;
            if (transport instanceof Car) {
                type = TransportType.CAR.toString();
            } else if (transport instanceof Moto) {
                type = TransportType.MOTO.toString();
            } else {
                System.out.println("model.Transport type not handled");
                return;
            }

            byte[] buffer = type.getBytes(charset);
            stream.writeInt(buffer.length);
            stream.write(buffer);

            buffer = transport.getMark().getBytes(charset);
            stream.writeInt(buffer.length);
            stream.write(buffer);

            Map<String, Double> models = transport.getModelsMap();

            stream.writeInt(models.size());

            models.forEach((k, v) -> {
                byte[] b = k.getBytes(charset);
                try {
                    stream.writeInt(b.length);
                    stream.write(b);
                    stream.writeDouble(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }});

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Transport inputTransport(InputStream in) {
        Transport transport = null;
        try (DataInputStream inputStream = new DataInputStream(in)) {

            String typeString = readString(inputStream);
            transport = getTransport(typeString);

            String mark = readString(inputStream);
            transport.setMark(mark);

            int count = inputStream.readInt();

            for (int i = 0; i < count; i++) {
                String model = readString(inputStream);
                double price = inputStream.readDouble();
                transport.addModel(model, price);
            }


        } catch (IOException | DuplicateModelNameException e) {
            e.printStackTrace();
        }

        return transport;
    } //DataInputStrea


    public static void writeTransport(Transport transport, Writer out) {
        PrintWriter writer = new PrintWriter(out);
        String type = null;
        if (transport instanceof Car) {
            type = TransportType.CAR.toString();
        } else if (transport instanceof Moto) {
            type = TransportType.MOTO.toString();
        } else {
            System.out.println("model.Transport type not handled");
            return;
        }

        writer.println(type);
        writer.println(transport.getMark());
        Map<String, Double> map = transport.getModelsMap();
        writer.println(map.size());
        map.forEach((k, v) -> {
                    writer.println(k);
                    writer.println(v);
                }
        );
        writer.flush();
    }//PrintWriter

    public static Transport readTransport(Reader in) {
        Transport transport = null;
        try (
                BufferedReader reader = new BufferedReader(in);
        ) {
            String type = reader.readLine();
            transport = getTransport(type);

            String mark = reader.readLine();
            transport.setMark(mark);

            int count = Integer.parseInt(reader.readLine());

            for (int i = 0; i < count; i++) {
                String model = reader.readLine();
                double price = Double.parseDouble(reader.readLine());
                transport.addModel(model, price);
            }
        } catch (IOException | DuplicateModelNameException e) {
            e.printStackTrace();
        }
        return transport;
    }

    private static Transport getTransport(String typeString) {
        Transport transport;
        if (typeString.equals(TransportType.CAR.toString())) {
            transport = new Car();
        } else if (typeString.equals(TransportType.MOTO.toString())) {
            transport = new Moto();
        } else {
            throw new RuntimeException("No such transport type");
        }
        return transport;
    }
    
    /*
    В обоих случаях нужно записать марку транспортного средства, количество моделей, а затем список моделей и цен моделей. 
    При записи строки в байтовый поток использовать метод getBytes() для перевода строки в массив байт. Перед строкой нужно записать её длину.

    Проверить возможности методов (в методе main), в качестве реальных потоков используя файловые потоки
    (FileInputStream, FileOutputStream, FileReader и FileWriter), а также потоки System.in и System.out.
     */

    private static String readString(DataInputStream inputStream) throws IOException {
        int typeLength = inputStream.readInt();
        byte[] type = inputStream.readNBytes(typeLength);
        return new String(type, charset);
    }


    public static Car generateCar() throws DuplicateModelNameException {
        Car car = new Car();
        car.addModel("Model 1", 1.0);
        car.addModel("Model 2", 2.0);
        car.addModel("Model 3", 3.0);
        return car;
    }

    public static Moto generateMoto() throws DuplicateModelNameException {
        Moto car = new Moto();
        car.addModel("Model 1", 1.0);
        car.addModel("Model 2", 2.0);
        car.addModel("Model 3", 3.0);
        return car;
    }

}
