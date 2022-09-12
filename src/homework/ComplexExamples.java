package homework;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ComplexExamples {

    static class Person {
        final int id;

        final String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person person)) return false;
            return getId() == person.getId() && getName().equals(person.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getName());
        }
    }

    private static Person[] RAW_DATA = new Person[]{
            new Person(0, "Harry"),
            new Person(0, "Harry"), // дубликат
            new Person(1, "Harry"), // тёзка
            new Person(2, "Harry"),
            new Person(3, "Emily"),
            new Person(4, "Jack"),
            new Person(4, "Jack"),
            new Person(5, "Amelia"),
            new Person(5, "Amelia"),
            new Person(6, "Amelia"),
            new Person(7, "Amelia"),
            new Person(8, "Amelia"),
    };
        /*  Raw data:

        0 - Harry
        0 - Harry
        1 - Harry
        2 - Harry
        3 - Emily
        4 - Jack
        4 - Jack
        5 - Amelia
        5 - Amelia
        6 - Amelia
        7 - Amelia
        8 - Amelia

        **************************************************

        Duplicate filtered, grouped by name, sorted by name and id:

        Amelia:
        1 - Amelia (5)
        2 - Amelia (6)
        3 - Amelia (7)
        4 - Amelia (8)
        Emily:
        1 - Emily (3)
        Harry:
        1 - Harry (0)
        2 - Harry (1)
        3 - Harry (2)
        Jack:
        1 - Jack (4)
     */

    public static void main(String[] args) {
        System.out.println("Raw data:");
        System.out.println();

        for (Person person : RAW_DATA) {
            System.out.println(person.id + " - " + person.name);
        }

        System.out.println();
        System.out.println("**************************************************");
        System.out.println("Task #1");
        System.out.println("Duplicate filtered, grouped by name, sorted by name and id:");
        System.out.println();

        /*
        Task1
            Убрать дубликаты, отсортировать по идентификатору, сгруппировать по имени

            Что должно получиться
                Key:Amelia
                Value:4
                Key: Emily
                Value:1
                Key: Harry
                Value:3
                Key: Jack
                Value:1
         */

        // Убраем дубликаты, отсортируем по идентификатору, группируем по имени
        Arrays.stream(RAW_DATA)
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparingInt(Person::getId))
                .collect(Collectors.groupingBy(Person::getName))
                .forEach((s, personList) -> System.out.printf("Key: %s%nValue: %s%n", s, personList.size()));

        /*
        Task2

            [3, 4, 2, 7], 10 -> [3, 7] - вывести пару менно в скобках, которые дают сумму - 10
         */

        System.out.println();
        System.out.println("**************************************************");
        System.out.println("Task #2");
        System.out.println("[3, 4, 2, 7], 10 -> [3, 7] - вывести пару менно в скобках, которые дают сумму - 10");
        System.out.println();

        // Выводит первую пару удовлетворяющую условию
        BiConsumer<List<Integer>, Integer> showFirstPairCondition = (list, i) -> {
            Set<List<Integer>> pairs = new LinkedHashSet<>();
            IntStream.range(0, list.size()).forEach(j -> list.stream().skip(j).forEachOrdered(v -> {
                        if (v + list.get(j) == i) {
                            List<Integer> aList = new LinkedList<>();
                            aList.add(list.get(j));
                            aList.add(v);
                            pairs.add(aList);
                        }
                    }));
            System.out.println(pairs.stream().findFirst().orElseGet(ArrayList::new));
        };

        int[] integers = new int[]{3,4,2,7};
        int condition = 10;

        System.out.format("Source: %s, %s -> Result: ", Arrays.toString(integers), condition);
        showFirstPairCondition.accept(Arrays.stream(integers).boxed().toList(), condition);

        /*
        Task3
            Реализовать функцию нечеткого поиска
                    fuzzySearch("car", "ca6$$#_rtwheel"); // true
                    fuzzySearch("cwhl", "cartwheel"); // true
                    fuzzySearch("cwhee", "cartwheel"); // true
                    fuzzySearch("cartwheel", "cartwheel"); // true
                    fuzzySearch("cwheeel", "cartwheel"); // false
                    fuzzySearch("lw", "cartwheel"); // false
         */

        System.out.println();
        System.out.println("**************************************************");
        System.out.println("Task #3");
        System.out.println("Реализовать функцию нечеткого поиска");
        System.out.println();

        // Реализация функции нечеткого поиска
        BiFunction<String, String, Boolean> fuzzySearch = (s, t) -> {
            int index = 0;
            int count = 0;
            for (int i = 0; i < s.length(); i++) {
                boolean isFound = false;
                for (int j = index; j < t.length(); j++) {
                    if (s.charAt(i) == t.charAt(j)) {
                        index = j+1;
                        count++;
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) return false;
            }
            return count == s.length();
        };

        // Проверка функции и вывод результатов
        Map<String, String> map = new LinkedHashMap<>();
        map.put("car", "ca6$$#_rtwheel");
        map.put("cwhl", "cartwheel");
        map.put("cwhee", "cartwheel");
        map.put("cartwheel", "cartwheel");
        map.put("cwheeel", "cartwheel");
        map.put("lw", "cartwheel");
        map.put("1. При число. отдельного репозитория ()", """
                1. При выполнении ДЗ стримами пользоваться не обязательно. Лаконичность и красота кода при этом важны.
                2. Задача №2 - на вход подается массив и число. Нужно вывести первую пару чисел, которые дают в сумме число.
                3. ДЗ выкладывать на гитхаб в виде отдельного репозитория (можно просто ссылку на репозиторий, можно на МР)""");

        map.forEach((s, s2) -> System.out.format("%n\"%s\" -> \"%s\": %n fuzzySearch() > %s%n", s, s2, fuzzySearch.apply(s, s2)));

    }
}
