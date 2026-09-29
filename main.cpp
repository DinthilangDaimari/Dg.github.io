#include <iostream>
#include <random>
using namespace std;

int main() {
    random_device rd;
    mt19937 gen(rd());
    int secret = uniform_int_distribution<int>(1, 100)(gen);
    int guess, tries = 0;

    cout << "=== Guess the Number (1-100) ===\n";
    while (true) {
        cout << "Your guess: ";
        if (!(cin >> guess)) {
            cout << "\nNo more input. The number was " << secret << "\n";
            return 0;
        }
        tries++;
        if (guess < secret) cout << "Too low!\n";
        else if (guess > secret) cout << "Too high!\n";
        else {
            cout << "Correct! You won in " << tries << " tries.\n";
            return 0;
        }
    }
}
