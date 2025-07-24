#!/bin/bash
cd /home/kavia/workspace/code-generation/tic-tac-toe-game-5289/tic_tac_toe_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

