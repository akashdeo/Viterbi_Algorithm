/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package nlphomework3;

import java.util.Scanner;

/**
 *
 * @author akash
 */
public class Viterbi {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] states = {"HOT", "COLD"};
        int[] observations = {1, 2, 3};
        double[] initialProbabilities = {0.8, 0.2};
        String[][] stateTransitions = {{"0", "HOT", "COLD"}, {"HOT", "0.7", "0.3"}, {"COLD", "0.4", "0.6"}};
        String[][] observationTransitions = {{"0", "1", "2", "3"}, {"HOT", "0.2", "0.4", "0.4"}, {"COLD", "0.5", "0.4", "0.1"}};
        System.out.println("Enter the observation sequence");
        String givenObservation = sc.next();
        String[][] probabilityMatrix = new String[states.length + 1][givenObservation.length() + 1];
        String[][] backtrackMatrix = new String[states.length + 1][givenObservation.length() + 1];
        probabilityMatrix[0][0] = " ";
        backtrackMatrix[0][0] = " ";
        for (int len = 0; len < states.length; len++) {
            probabilityMatrix[len + 1][0] = states[len];
            backtrackMatrix[len + 1][0] = states[len];
        }
        for (int len = 0; len < givenObservation.length(); len++) {
            probabilityMatrix[0][len + 1] = givenObservation.charAt(len) + "";
            backtrackMatrix[0][len + 1] = givenObservation.charAt(len) + "";
        }
        for (int col = 1; col < givenObservation.length() + 1; col++) {
            for (int row = 1; row < states.length + 1; row++) {
                if (col == 1) {
                    String leftSide = probabilityMatrix[row][0];
                    String upSide = probabilityMatrix[0][col];
                    g:
                    {
                        for (int r = 0; r < states.length + 1; r++) {
                            for (int c = 0; c < observations.length + 1; c++) {
                                if (observationTransitions[r][0].equals(leftSide) && observationTransitions[0][c].equals(upSide)) {
                                    if (probabilityMatrix[row][0].equals("HOT")) {
                                        probabilityMatrix[row][col] = initialProbabilities[0] * Double.parseDouble(observationTransitions[r][c]) + "";
                                        backtrackMatrix[row][col] = "LH";
                                        break g;

                                    } else {
                                        if (probabilityMatrix[row][0].equals("COLD")) {
                                            probabilityMatrix[row][col] = initialProbabilities[1] * Double.parseDouble(observationTransitions[r][c]) + "";
                                            backtrackMatrix[row][col] = "LC";
                                            break g;
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    String leftSide = probabilityMatrix[row][0];
                    String upSide = probabilityMatrix[0][col];
                    for (int r = 0; r < states.length + 1; r++) {
                        for (int c = 0; c < observations.length + 1; c++) {
                            if (observationTransitions[r][0].equals(leftSide) && observationTransitions[0][c].equals(upSide)) {
                                if (row == 1) {

                                    probabilityMatrix[row][col] = Double.parseDouble(observationTransitions[r][c]) * Math.max(Double.parseDouble(probabilityMatrix[row][col - 1]) * Double.parseDouble(stateTransitions[1][1]), Double.parseDouble(probabilityMatrix[row + 1][col - 1]) * Double.parseDouble(stateTransitions[2][1])) + "";
                                    double firstValue = Double.parseDouble(probabilityMatrix[row][col - 1]) * Double.parseDouble(stateTransitions[1][1]);
                                    double secondValue = Double.parseDouble(probabilityMatrix[row + 1][col - 1]) * Double.parseDouble(stateTransitions[2][1]);
                                    if (firstValue > secondValue) {
                                        backtrackMatrix[row][col] = "LH";
                                    } else {
                                        backtrackMatrix[row][col] = "BD";
                                    }
                                } else {
                                    if (row == 2) {

                                        probabilityMatrix[row][col] = Double.parseDouble(observationTransitions[r][c]) * Math.max(Double.parseDouble(probabilityMatrix[row - 1][col - 1]) * Double.parseDouble(stateTransitions[1][2]), Double.parseDouble(probabilityMatrix[row][col - 1]) * Double.parseDouble(stateTransitions[2][2])) + "";
                                        double firstValue = Double.parseDouble(probabilityMatrix[row - 1][col - 1]) * Double.parseDouble(stateTransitions[1][2]);
                                        double secondValue = Double.parseDouble(probabilityMatrix[row][col - 1]) * Double.parseDouble(stateTransitions[2][2]);
                                        if (firstValue > secondValue) {
                                            backtrackMatrix[row][col] = "UD";
                                        } else {
                                            backtrackMatrix[row][col] = "LC";
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (int row = 0; row < states.length + 1; row++) {
            for (int col = 0; col < givenObservation.length() + 1; col++) {

                System.out.print(probabilityMatrix[row][col] + "\t\t");

            }
            System.out.println();
        }
        System.out.println();
        String str = "";
        String Answer = "";
        double value = Math.max(Double.parseDouble(probabilityMatrix[1][givenObservation.length()]), Double.parseDouble(probabilityMatrix[2][givenObservation.length()]));
        if (Double.parseDouble(probabilityMatrix[1][givenObservation.length()]) > Double.parseDouble(probabilityMatrix[2][givenObservation.length()])) {
            str = backtrackMatrix[1][givenObservation.length()];
        } else {
            str = backtrackMatrix[2][givenObservation.length()];
        }
        int i = givenObservation.length();

        while (i > 0) {
            if (str.equals("LH")) {
                str = backtrackMatrix[1][i - 1];
                Answer += "HOT ";

            } else if (str.equals("UD")) {
                str = backtrackMatrix[1][i - 1];
                Answer += "COLD ";
            } else if (str.equals("BD")) {
                str = backtrackMatrix[2][i - 1];
                Answer += "HOT ";
            } else if (str.equals("LC")) {
                str = backtrackMatrix[2][i - 1];
                Answer += "COLD ";
            }

            i--;
        }
        String ans = "";
        String[] AnswerMatrix = Answer.split(" ");

        for (int len = AnswerMatrix.length - 1; len >= 0; len--) {
            ans += AnswerMatrix[len] + " ";
        }
        System.out.println("Final Probability: " + Math.max(Double.parseDouble(probabilityMatrix[1][givenObservation.length()]), Double.parseDouble(probabilityMatrix[2][givenObservation.length()])));
        System.out.println("Hidden States: " + ans);
    }

}
