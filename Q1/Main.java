package worksapplication;

import java.util.*;


public class Main {

    private static boolean[][] digit_letter_map;  //row: digit index;   col: letter index;
	private static int n,m,k;
	private static int bestResult, bestSoFar;

    public static void main(String[] args) {
        try{
            getInputs();
			//We filter through duplicates AND COMPLEMENTARIES
			HashSet<Num> set = new HashSet<>();
			for(int i = 0; i < digit_letter_map.length; ++i){
				Num newComer = new Num(digit_letter_map[i]);

				boolean notComplementary = true;
				for(Num n : set){
					//If no improvement, meaning complementary
					if(Num.uniqueCombination(newComer, n) == 0)
						notComplementary = false;
				}

				if(notComplementary)
					set.add(newComer);
			}

			//Simple cases
			if(k == 2){
				System.out.println(1);
				return;
			} else if (k == 3){
				System.out.println(2);
				return;
			}

			//Transform into a sorted list
			bestResult = k < 5? 2 : 3;
			bestSoFar = 6;


			List<Num> digitInfo = new ArrayList<>(set);
			digitInfo.sort(new Comparator<Num>() {
				//We favour those with half of 1
				//The closer to have half number of 1, the smaller it is considered (to be in front of sorted array)
				@Override
				public int compare(Num o1, Num o2) {
					int mid = o1.letterDigits.length;
					return Math.abs(o1.numberOfTrue*2 - mid) - Math.abs(o2.numberOfTrue*2 - mid);
				}
			});

			//Collections.sort(digitInfo);

			//Num[] sortedDigitInfo = (Num[])digitInfo.toArray();
			for(int i = 0; i < digitInfo.size(); ++i){
				List<Num> remainingDigits = new ArrayList<Num>(digitInfo);

				//The selected one
				List<Num> selected = new ArrayList<>();
				selected.add(remainingDigits.remove(i));

				//Sort by how different it is from first column
				remainingDigits.sort(new Comparator<Num>() {
					//We favour those about half as different as the first Num
					@Override
					public int compare(Num o1, Num o2) {
						return Num.uniqueCombination(selected, o2) - Num.uniqueCombination(selected, o1);
					}
				});

				for(int j = 0; j < remainingDigits.size(); ++j){
					List<Num> lastDigits = new ArrayList<Num>(remainingDigits);
					Num potentialSecond = lastDigits.remove(j);
					selected.add(potentialSecond);

					//Is this second good enough?
					//Sort by how different it is from first two columns
					lastDigits.sort(new Comparator<Num>() {
						@Override
						public int compare(Num o1, Num o2) {
							return Num.uniqueCombination(selected, o2) - Num.uniqueCombination(selected, o1);
						}
					});

					//List<Num> more = new ArrayList<>();
					List<Num> res = validate(selected, lastDigits);
					if(res == null){
						continue;
					}
					else if(res.size() == bestResult){
						System.out.println(bestResult);
						return;
					}

					//Remove from list and start over
					selected.remove(potentialSecond);
				}
			}

			System.out.println(bestSoFar);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //Using bestResult and bestSoFar to judge progress

	//Depth first. i.e. keep adding until good or max out, then back to base case
    private static List<Num> validate(List<Num> selected, List<Num> remaining){
		//If this is already worse than we have
		if(selected.size() >= bestSoFar)
			return null;
		if(Num.uniqueCombination(selected) == k){
			//If it is the best imaginable, then return!!
			if(selected.size() == bestResult)
				return selected;
				//Else if can update best results, also can!
			else if(selected.size() < bestSoFar){
				bestSoFar = selected.size();
				return selected;
			}
		}

		List<Num> currentBest = null;

		for(Num n : remaining){
			selected.add(n);

			//need to keep searching
			List<Num> newRemaining = new ArrayList<>(remaining);
			newRemaining.add(n);
			List<Num> result = validate(selected, newRemaining);
			if(result == null)
				continue;
			if(currentBest == null || result.size() < currentBest.size()){
				currentBest = result;
			}

			selected.remove(n);
		}
		return currentBest;
	}

    private static void getInputs() throws Exception{
        Scanner sc = new Scanner(System.in);
        String[] firstLine = sc.nextLine().split(" ");

        n = Integer.parseInt(firstLine[0]);
        m = Integer.parseInt(firstLine[1]);
        k = Integer.parseInt(firstLine[2]);
        digit_letter_map = new boolean[n*m][k];

        for(int i = 0; i < k; ++i){
            sc.nextLine();
            for(int j = 0; j < n; ++j){
                String l = sc.nextLine();
                for(int s = 0; s < m; s++){
                    if(Integer.parseInt(String.valueOf(l.charAt(s))) == 1){
                        digit_letter_map[m*j+s][i] = true;
                    } else if(Integer.parseInt(String.valueOf(l.charAt(s))) == 0){
                        digit_letter_map[m*j+s][i] = false;
                    } else throw new Exception("Parse Failure: " + l.charAt(s));
                }
            }
        }
    }
}

class Num{
	final boolean[] letterDigits;  // length 2 - 6
	final int numberOfTrue;

	Num(boolean[] d){
		this.letterDigits = d;
		int res = 0;
		for(int i = 0; i < letterDigits.length; ++i){
			if(letterDigits[i])
				++res;
		}
		numberOfTrue = res;
	}

	public boolean getAt(int i){
		return letterDigits[i];
	}

	@Override
	public int hashCode(){ //actually gives the decimal number
		int res = 0, i = 0;
		while(i < letterDigits.length){
			res += letterDigits[i]? Math.pow(2, i) : 0;
			++i;
		}
		return res;
	}

	static int uniqueCombination(Num... nums){
		List<Num> z = Arrays.asList(nums);
		return uniqueCombination(z);
	}

	static int uniqueCombination(List<Num> nums, Num another){
		List<Num> copied = new ArrayList<>(nums);
		copied.add(another);
		return uniqueCombination(copied);
	}

	static int uniqueCombination(List<Num> nums){
		HashSet<Integer> set = new HashSet<>();

		//Reconstruct the integers
		int equi, pow;
		for(int i = 0; i < nums.get(0).letterDigits.length; ++i) {
			equi = 0;
			pow = 0;
			for (Num n : nums) {
				if(n.letterDigits[i])
					equi+=Math.pow(2, pow);
				pow++;
			}
			set.add(equi);
		}

		return set.size();
	}
}

