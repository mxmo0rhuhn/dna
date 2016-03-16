import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int nrTest = scanner.nextInt();
        for (int i = 0; i < nrTest; i++) {

            int nrElem = scanner.nextInt();

            ArrayList<Integer> cur = new ArrayList<Integer>();
            for (int j = 0; j < nrElem; j++) {
                cur.add(scanner.nextInt());
            }

            blum(cur, true);
        }

        scanner.close();
    }

    /**
     * In dieser Aufgabe soll der Algorithmus von Blum zur Medianberechnung implementiert werden.
     * Sei x_1 , ..., x_n eine Folge mit n > 5 Elementen (Duplikate sind erlaubt). Der Algorithmus führt
     * die folgenden Schritte aus, um das k-kleinste Element zu finden.
     * <p/>
     * Unser Ziel ist die Berechnung des Medians, d.h. des dn/2e-kleinsten Elements. Für die Sequenz
     * 3, 4, 2, 6, 4, 7, 1 ist der Median 4.
     */
    private static int blum(List<Integer> list, boolean print) {

        // Java indexing - starts at 0
        int k = new Double(Math.ceil(list.size() / 2.0)).intValue();

        Integer medianDerMediane = getMedian(list, print);

        if (print) {
            System.out.print(medianDerMediane + " ");
        }

        int pm = quickSelect(list, medianDerMediane);

        /*
         * 5) Falls k = p_m , dann wissen wir, dass das Pivotelement sich auf der gesuchten Position
         * befindet, und wir liefern m zurück. Ist dagegen k < p_m , dann befindet sich das k-kleinste
         * Element links vom m, und wir suchen rekursiv nach dem k-kleinsten Element unter den
         * p m − 1 Elementen links von m. Ansonsten ist k > p m , und wir suchen rekursiv nach dem
         * (k − p m )-kleinsten Element unter den n − p m Elementen rechts vom m.
         */
        while ((pm + 1) != k) {
            if (k < (pm + 1)) {
                // Ist dagegen k < p_m , dann befindet sich das k-kleinste
                // Element links vom m, und wir suchen rekursiv nach dem k-kleinsten Element unter den
                // p_m − 1 Elementen links von m.

                // pm in smaller suchen
                // between the specified fromIndex, inclusive, and toIndex, exclusive
                list = list.subList(0, pm);
                medianDerMediane = getMedian(list, false);
                pm = quickSelect(list, medianDerMediane);

            } else if (k > (pm + 1)) {
                // Ansonsten ist k > p m , und wir suchen rekursiv nach dem
                // (k − p_m )-kleinsten Element unter den n − p_m Elementen rechts vom m.

                // k-pm in bigger suchen
                // between the specified fromIndex, inclusive, and toIndex, exclusive
                list = list.subList(pm + 1, list.size());
                medianDerMediane = getMedian(list, false);

                k = k - (pm + 1);
                pm = quickSelect(list, medianDerMediane);
            }
        }
        if (print) {
            System.out.println(list.get(pm));
        }
        return list.get(pm);
    }

    /**
     * 1) Teile die Elemente der Reihe nach in abgerundet ( n/ 5 ) Fünfergruppen sowie maximal
     * eine Gruppe mit den verbleibenden n mod 5 Elementen auf. So sind die ersten fünf Elemente
     * in der ersten Gruppe, usw.
     * <p/>
     * 2) Für jede der obigen Gruppen, berechne den Median der Gruppe. Für eine Gruppe mit 2
     * Elementen ist der Median das kleinere Element, für eine Gruppe mit 4 Elementen ist der
     * Median das zweitkleinste Element.
     * <p/>
     * 3) Berechne den Median m der Mediane aus dem vorigen Schritt rekursiv. Dieses Element
     * wird Median der Mediane genannt.
     *
     * @param list  List to get the median of
     * @param print shall I print intermediate results?
     * @return the median element
     */
    private static int getMedian(List<Integer> list, boolean print) {
        int nrElem = list.size();
        ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>();

        // In Gruppen aufteilen
        int j = 0;
        while (j < nrElem) {
            ArrayList<Integer> cur = new ArrayList<Integer>();

            for (int k = 0; k < 5 && nrElem > j; k++) {
                cur.add(list.get(j));
                j++;
            }
            groups.add(cur);
        }

        // Median der Gruppen ermitteln
        ArrayList<Integer> mediane = new ArrayList<Integer>();
        for (ArrayList<Integer> cur : groups) {
            Collections.sort(cur);
            Integer med = cur.get((new Double(Math.ceil((cur.size() - 1) / 2)).intValue()));
            mediane.add(med);
        }
        if (print) {
            for (Integer med : mediane) {
                System.out.print(med + " ");
            }
        }

        if (mediane.size() > 1) {
            return blum(mediane, false);
        }
        return mediane.get(0);
    }

    /**
     * 4) Führe den Aufteilungsschritt von Quickselect durch, um das Element m an die korrekte
     * Position p_m in der sortierten Folge zu bringen. Dann gibt es p_m − 1 Elemente links von m
     * (mit Wert höchstens m) und n − p_m Elemente rechts von m (mit Wert mindestens m).
     *
     * @param list  list to operate quick select on
     * @param pivot pivot element
     * @return index of the pivot element
     */
    private static int quickSelect(List<Integer> list, Integer pivot) {

        int leftIndex = 0;
        int rightIndex = list.size() - 1;
        int pivotIndex = 0;

        boolean hasPivotIndex = false;
        boolean leftNext = true;
        boolean rightNext = true;

        Integer leftElement = 0;
        Integer rightElement = 0;
        while (leftIndex <= rightIndex) {
            if (leftNext) {
                leftElement = list.get(leftIndex);
            }
            if (rightNext) {
                rightElement = list.get(rightIndex);
            }

            // to get pivot index
            if (!hasPivotIndex && leftElement == pivot) {
                hasPivotIndex = true;
                pivotIndex = leftIndex;
            } else if (!hasPivotIndex && rightElement == pivot) {
                hasPivotIndex = true;
                pivotIndex = rightIndex;
            }

            if (leftElement <= pivot) {
                leftIndex++;
                leftNext = true;
            } else {
                leftNext = false;
            }

            if (rightElement >= pivot) {
                rightIndex--;
                rightNext = true;
            } else {
                rightNext = false;
            }

            if (leftIndex < rightIndex && !leftNext && !rightNext) {
                list.set(leftIndex, rightElement);
                list.set(rightIndex, leftElement);

                leftIndex++;
                rightIndex--;

                rightNext = true;
                leftNext = true;
            }
        }

        if (pivotIndex < rightIndex) {
            rightElement = list.get(rightIndex);
            list.set(pivotIndex, rightElement);
            list.set(rightIndex, pivot);
            return rightIndex;
        } else if (pivotIndex > leftIndex) {
            leftElement = list.get(leftIndex);
            list.set(pivotIndex, leftElement);
            list.set(leftIndex, pivot);
            return leftIndex;
        }
        return pivotIndex;
    }
}

