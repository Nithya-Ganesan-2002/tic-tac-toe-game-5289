package com.example.tictactoefrontend

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.gridlayout.widget.GridLayout

// PUBLIC_INTERFACE
class MainActivity : AppCompatActivity() {

    private var board = Array(3) { Array(3) { "" } }
    private var currentPlayer = "X"
    private var gameActive = true
    private var versusAI = false

    private lateinit var playerTurnText: TextView
    private lateinit var gameResultText: TextView
    private lateinit var gameGrid: GridLayout
    private lateinit var resetButton: Button
    private lateinit var newGameButton: Button
    private lateinit var twoPlayerRadio: RadioButton
    private lateinit var aiRadio: RadioButton

    // PUBLIC_INTERFACE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerTurnText = findViewById(R.id.playerTurnText)
        gameResultText = findViewById(R.id.gameResultText)
        gameGrid = findViewById(R.id.gameGrid)
        resetButton = findViewById(R.id.resetButton)
        newGameButton = findViewById(R.id.newGameButton)
        twoPlayerRadio = findViewById(R.id.twoPlayerRadio)
        aiRadio = findViewById(R.id.aiRadio)
        val modeRadioGroup = findViewById<RadioGroup>(R.id.modeRadioGroup)

        setupBoard()

        resetButton.setOnClickListener {
            resetBoard()
        }
        newGameButton.setOnClickListener {
            newGame()
        }

        modeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            versusAI = (checkedId == R.id.aiRadio)
            newGame()
        }
    }

    // PUBLIC_INTERFACE
    private fun setupBoard() {
        gameGrid.removeAllViews()
        board = Array(3) { Array(3) { "" } }
        for (i in 0..2) {
            for (j in 0..2) {
                val cell = Button(this)
                cell.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i, 1f)
                    columnSpec = GridLayout.spec(j, 1f)
                    width = 0
                    height = 0
                    setMargins(4, 4, 4, 4)
                }
                cell.setBackgroundColor(ContextCompat.getColor(this, R.color.white))
                cell.setTextColor(ContextCompat.getColor(this, R.color.primary))
                cell.textSize = 32f
                cell.text = ""
                cell.isEnabled = gameActive

                cell.setOnClickListener {
                    if (cell.text == "" && gameActive) {
                        cell.text = currentPlayer
                        cell.setTextColor(
                            ContextCompat.getColor(this,
                                if (currentPlayer == "X") R.color.primary else R.color.secondary
                            )
                        )
                        board[i][j] = currentPlayer
                        if (checkWin()) {
                            gameResultText.text = "Player $currentPlayer Wins!"
                            gameResultText.visibility = View.VISIBLE
                            gameActive = false
                        } else if (isDraw()) {
                            gameResultText.text = "It's a Draw!"
                            gameResultText.visibility = View.VISIBLE
                            gameActive = false
                        } else {
                            switchPlayer()
                            if (versusAI && currentPlayer == "O" && gameActive) {
                                aiMove()
                            }
                        }
                        updateTurnDisplay()
                    }
                }
                gameGrid.addView(cell)
            }
        }
        updateTurnDisplay()
        gameResultText.visibility = View.GONE
        gameActive = true
    }

    // PUBLIC_INTERFACE
    private fun updateTurnDisplay() {
        if (gameActive) {
            playerTurnText.text = "Player $currentPlayer's turn"
        }
    }

    // PUBLIC_INTERFACE
    private fun resetBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                board[i][j] = ""
                val idx = i * 3 + j
                val cell = gameGrid.getChildAt(idx) as Button
                cell.text = ""
                cell.isEnabled = true
                cell.setTextColor(ContextCompat.getColor(this, R.color.primary))
            }
        }
        gameActive = true
        currentPlayer = "X"
        gameResultText.text = ""
        gameResultText.visibility = View.GONE
        updateTurnDisplay()
        if (versusAI && currentPlayer == "O") aiMove()
    }

    // PUBLIC_INTERFACE
    private fun newGame() {
        currentPlayer = "X"
        gameActive = true
        board = Array(3) { Array(3) { "" } }
        setupBoard()
    }

    // PUBLIC_INTERFACE
    private fun switchPlayer() {
        currentPlayer = if (currentPlayer == "X") "O" else "X"
    }

    // PUBLIC_INTERFACE
    private fun checkWin(): Boolean {
        // Rows, columns, diags
        for (i in 0..2) {
            if (board[i][0] == currentPlayer &&
                board[i][1] == currentPlayer &&
                board[i][2] == currentPlayer) return true
            if (board[0][i] == currentPlayer &&
                board[1][i] == currentPlayer &&
                board[2][i] == currentPlayer) return true
        }
        if (board[0][0] == currentPlayer &&
            board[1][1] == currentPlayer &&
            board[2][2] == currentPlayer) return true
        if (board[0][2] == currentPlayer &&
            board[1][1] == currentPlayer &&
            board[2][0] == currentPlayer) return true
        return false
    }

    // PUBLIC_INTERFACE
    private fun isDraw(): Boolean {
        for (i in 0..2) for (j in 0..2)
            if (board[i][j].isEmpty()) return false
        return true
    }

    // PUBLIC_INTERFACE
    private fun aiMove() {
        // Simple AI: pick first available cell
        if (!gameActive) return
        for (i in 0..2) {
            for (j in 0..2) {
                if (board[i][j].isEmpty()) {
                    board[i][j] = currentPlayer
                    val idx = i * 3 + j
                    val cell = gameGrid.getChildAt(idx) as Button
                    cell.text = currentPlayer
                    cell.setTextColor(ContextCompat.getColor(this, R.color.secondary))
                    if (checkWin()) {
                        gameResultText.text = "Player $currentPlayer Wins!"
                        gameResultText.visibility = View.VISIBLE
                        gameActive = false
                    } else if (isDraw()) {
                        gameResultText.text = "It's a Draw!"
                        gameResultText.visibility = View.VISIBLE
                        gameActive = false
                    } else {
                        switchPlayer()
                    }
                    updateTurnDisplay()
                    return
                }
            }
        }
    }
}
