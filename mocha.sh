export NODE_PATH=src/nodejs/
mocha --compilers coffee:coffee-script -G -u tdd tests/nodejs
