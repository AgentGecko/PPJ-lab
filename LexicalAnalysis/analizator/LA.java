

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.util.Scanner;

public class LA {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException {

		FileInputStream fIn = null;
		ObjectInputStream oIn = null;

		fIn = new FileInputStream("data.ser");
		oIn = new ObjectInputStream(fIn);
		List<PairNFAState> laStructures = (LinkedList<PairNFAState>) oIn
				.readObject();

		BufferedReader line = new BufferedReader(new InputStreamReader(
				System.in));
		String procitano = new String();
		String program = new String();
		program = "";
		int emptyCounter = 0;
		procitano = line.readLine();
		while (procitano!=null) {

			program += procitano + "\n";
			procitano = line.readLine();

		}
		int lastIndex = 0;
		int unacceptableNFACounter;
		String currentLAState = laStructures.get(0).stateName;
		int newLinesRecognised = 1;
		Map<NFA, String> possibleWords = new LinkedHashMap<NFA, String>();
		List<String> matches = new LinkedList<String>();
		for (int i = 0; i < program.length(); i++) {
			String _currentChar = Character.toString(program.charAt(i));
			
			for (PairNFAState onePair : laStructures) {
				unacceptableNFACounter = 0;
				if (onePair.stateName.equals(currentLAState)) {
					for (NFA automat : onePair.nfaList) {
						if (currentStatesNotNULL(automat, _currentChar)) {
							if (automat.currentStates
									.contains(automat.finalState)) {
								possibleWords.put(automat,
										program.substring(lastIndex, i + 1));
							}
						} else
							unacceptableNFACounter++;

					}
					if (noAutomataCanContinue(unacceptableNFACounter, onePair)) {
						if (matchesFound(possibleWords)) {
							int maximumLength, minimumIndex;
							NFA automata = null;
							String recognisedWord = "";

							maximumLength = findMaxLength(possibleWords);
							minimumIndex = findMinIndex(onePair, possibleWords,
									maximumLength);

							for (Map.Entry<NFA, String> entry : possibleWords
									.entrySet()) {
								if (entry.getValue().length() == maximumLength
										&& onePair.nfaList.indexOf(entry
												.getKey()) == minimumIndex) {
									automata = entry.getKey();
									recognisedWord = entry.getValue();
									break;
								}
							}
							possibleWords.clear();

							lastIndex = lastIndex + recognisedWord.length();

							i = lastIndex - 1;
							for (String rule : automata.rules) {
								String newGlobalState;
								int groupOnly;
								if (isNewRow(rule)) {
									newLinesRecognised++;
								}
								newGlobalState = isChangeState(rule);
								if (!newGlobalState
										.equals("NIJE_UDJI_U_STANJE")) {
									currentLAState = newGlobalState;
								}
								groupOnly = isGoBackX(rule);
								if (groupOnly != -1) 
								{
									i -= recognisedWord.length() - groupOnly;
									lastIndex= i+1;
									recognisedWord = recognisedWord.substring(0, groupOnly );
								}

							}
							matches.add(automata.name + " "+  newLinesRecognised +" " + recognisedWord);

						} else {
							i = lastIndex;
							lastIndex++;

						}
						resetNFAList(onePair);
						break;
					}
				}
			}

		}
		for (String s : matches) {
			if (!s.startsWith("none"))
				System.out.println(s);
		}
		oIn.close();
	}

	// These are the methods for checking whether one NFA has certain rules
	// which apply when it is in the accepted state
	public static boolean isNewRow(String rule) {
		if (rule.contains("NOVI_REDAK"))
			return true;
		else
			return false;
	}

	public static String isChangeState(String rule) {
		if (rule.contains("UDJI_U_STANJE"))
			return rule.split(" ")[1];
		else
			return "NIJE_UDJI_U_STANJE";
	}

	public static int isGoBackX(String rule) {
		if (rule.contains("VRATI_SE"))
			return Integer.parseInt(rule.split(" ")[1]);
		else
			return -1;
	}

	// This method checks whether automata of the current LA state can continue
	public static boolean noAutomataCanContinue(int counter,
			PairNFAState onePair) {
		return counter == onePair.nfaList.size();
	}

	// This method checks whether lexical entity matches were found so far
	public static boolean matchesFound(Map<NFA, String> possibleWords) {
		return !possibleWords.isEmpty();
	}

	// This method checks whether the current automata cannot do the transitions
	// and thus ends up with currentStates list equal to null
	public static boolean currentStatesNotNULL(NFA automat, String currentString) {
		if (automat.currentStates != null) {
			automat.currentStates = automat.nextState(currentString);
			if (automat.currentStates != null) {
				automat.emptyStringTransition();
				return true;
			} else
				return false;
		} else
			return false;
	}

	// This method returns the length of the longest word recognised
	public static int findMaxLength(Map<NFA, String> possibleWords) {
		int maxLength = -1;
		for (Map.Entry<NFA, String> entry : possibleWords.entrySet()) {
			if (entry.getValue().length() > maxLength)
				maxLength = entry.getValue().length();

		}
		return maxLength;
	}

	// This method finds the NFA which is preferred among the ones that
	// recognise a word with maximum length
	public static int findMinIndex(PairNFAState onePair,
			Map<NFA, String> possibleWords, int max_duljina) {
		int alo, min_ind = 9999999;
		for (Map.Entry<NFA, String> entry : possibleWords.entrySet()) {
			if (entry.getValue().length() == max_duljina) {
				alo = onePair.nfaList.indexOf(entry.getKey());
				if (alo < min_ind)
					min_ind = alo;
			}
		}
		return min_ind;
	}

	// This method resets all the NFA's of the LA state (Sets their current
	// state to start state + all empty string transitions possible at that
	// time)
	public static void resetNFAList(PairNFAState onePair) {
		for (NFA automat : onePair.nfaList) {
			automat.Reset();
		}
	}
}
