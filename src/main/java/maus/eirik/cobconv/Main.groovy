package maus.eirik.cobconv

import static maus.eirik.cobconv.Config.*

class Main {
    private Config config

    Main(Config config) {
        this.config = config
    }

    def  readFromProperties() {

    }
    public static void main(String[] args) {
        new Main(parse(args)).run System.out;
    }

    def run(OutputStream output) {

    }
}
