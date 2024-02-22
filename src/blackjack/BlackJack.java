package blackjack;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

public class BlackJack {
	List<Card> deck;
	Random rand = new Random();
	Object obj = new Object();
	
	// dealer
	Card hiddenCard;
	List<Card> dealersHand;
	int dealerSum;
	int dealerAcesCount;
	
	// player
	List<Card> playersHand;
	int playerSum;
	int playerAcesCount;
	
	
	final int CARD_WIDTH = 110;
	final int CARD_HEIGHT = 154;
	
	// Window
	int boardWidth = 800;
	int boardHeight = 600;
	
	JFrame frame = new JFrame("Black Jack");
	JPanel gamePanel = new JPanel() {
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			try {
				 //Draw the dealer hand

				Image hiddenCardImg = ImageIO.read(getClass().getResource("/images/back.png"));
				if (!stayButton.isEnabled()) {
					hiddenCardImg = ImageIO.read(getClass().getResource("/images/" + hiddenCard.toString() + ".png"));
				}
				g.drawImage(hiddenCardImg, 20, 20, CARD_WIDTH, CARD_HEIGHT, null);
				
				for (int i = 0; i < dealersHand.size(); i++) {
					Card card = dealersHand.get(i);
					Image cardImg = ImageIO.read(getClass().getResource("/images/" + card.toString() + ".png"));
					g.drawImage(cardImg, 20 + (CARD_WIDTH+10) * (i+1), 20, CARD_WIDTH, CARD_HEIGHT, null);
				}
					//Draw the player's hand
				for (int i = 0; i < playersHand.size(); i++) {
					Card card = playersHand.get(i);
					Image cardImg = ImageIO.read(getClass().getResource("/images/" + card.toString() + ".png"));
					g.drawImage(cardImg, 20 + (CARD_WIDTH+10) * (i), boardHeight - (CARD_HEIGHT + 100), CARD_WIDTH, CARD_HEIGHT, null);
				}
				
				if (!stayButton.isEnabled()) {
					dealerSum = reduceDealerAce();
					playerSum = reducePlayerAce();
					redealButton.setEnabled(true);
					System.out.println("STAY: ");
					System.out.println(dealerSum);
					System.out.println(playerSum);
					
					String message = "";
					if (playerSum > 21) {
						message = "You Lose.";
					} else if (dealerSum > 21 || playerSum > dealerSum) {
						message = "You Win!";
					} else if (playerSum == dealerSum) {
						message = "Push!";
					} else if (playerSum < dealerSum) {
						message = "You Lose.";
					}
					
					g.setFont(new Font("Arial", Font.BOLD, 30));
					g.setColor(Color.white);
					g.drawString(message, 320,250);
					g.drawString(String.valueOf(playerSum), 350, 290);
					g.drawString(String.valueOf(dealerSum), 350, 210);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};
	
	JPanel buttonPanel = new JPanel();
	JButton redealButton = new JButton("Redeal");
	JButton hitButton = new JButton("Hit");
	JButton stayButton = new JButton("Stay");
	
	
	// Constructor
	BlackJack() {
		frame.setVisible(true);
		frame.setSize(boardWidth,boardHeight);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		gamePanel.setLayout(new BorderLayout());
		gamePanel.setBackground(new Color(53,101,77));
		frame.add(gamePanel);
		
		redealButton.setFocusable(false);
		hitButton.setFocusable(false);
		stayButton.setFocusable(false);
		buttonPanel.add(redealButton);
		buttonPanel.add(hitButton);
		buttonPanel.add(stayButton);
		frame.add(buttonPanel, BorderLayout.SOUTH);

		// Create an actionListener for the Hit Button
		hitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Card card = dealCard();
				playerSum += card.getValue();
				playerAcesCount += card.isAce() ? 1 : 0;
				playersHand.add(card);
				if (reducePlayerAce() > 21) {
					dealersTurn();
				}
				
				gamePanel.repaint();
			}
		});
		
		// Create an actionListener for the Stay Button
		stayButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dealersTurn();
			}
		});
		
		// Create an action Listener for the Redeal Button
		redealButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startGame();
				gamePanel.repaint();
			}
		});
		
		

	}
	private void dealersTurn() {
		hitButton.setEnabled(false);
		stayButton.setEnabled(false);
		
		while (dealerSum < 17 && playerSum < 21) {
			gamePanel.repaint();

			Card card = dealCard();
			dealerSum += card.getValue();
			dealerAcesCount += card.isAce()? 1 : 0;
			dealersHand.add(card);
		}
		
		gamePanel.repaint();
	}

	public void startGame() {
		hitButton.setEnabled(true);
		stayButton.setEnabled(true);
		redealButton.setEnabled(false);
		// Build and shuffle deck of cards.
		buildDeck();
		shuffleDeck();
		
		// Dealers actions
		dealersHand = new ArrayList<>();
		dealerSum = 0;
		dealerAcesCount = 0;
		
		hiddenCard = dealCard();
		dealerSum += hiddenCard.getValue();
		dealerAcesCount += hiddenCard.isAce() ? 1 : 0;
				
		Card card = dealCard();
		dealerSum += card.getValue();
		dealerAcesCount += card.isAce() ? 1 : 0;
		dealersHand.add(card);
		
		System.out.println("DEALER'S HAND");
		System.out.println(hiddenCard);
		System.out.println(dealersHand);
		System.out.println(dealerSum);
		System.out.println(dealerAcesCount);
		
		// Player actions
		playersHand = new ArrayList<>();
		playerSum = 0;
		playerAcesCount = 0;
		
		for (int i = 0; i < 2; i++) {
			card = dealCard();
			playerSum += card.getValue();
			playerAcesCount += card.isAce() ? 1 : 0;
			playersHand.add(card);
		}
		
		System.out.println("PLAYER'S HAND");
		System.out.println(playersHand);
		System.out.println(playerSum);
		System.out.println(playerAcesCount);
	}
	
	private Card dealCard() {
		return deck.remove(deck.size() - 1);
	}
	
	private void shuffleDeck() {
		List<Card> shuffledDeck = new ArrayList<>();
		
		while (deck.size() > 0) {
			int i = rand.nextInt(deck.size());
			shuffledDeck.add(deck.get(i));
			deck.remove(i);
		}
		deck = shuffledDeck;
		
		System.out.println("SHUFFLE DECK:");
		System.out.println(deck);
	}

	private void buildDeck() {
		deck = new ArrayList<Card>();
		String[] values = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k"};
		String[] suits = {"c", "d", "h", "s"};
		
		for (int i = 0; i < suits.length; i++) {
			for (int j = 0; j < values.length; j++) {
				Card card = new Card(values[j], suits[i]);
				deck.add(card);
			}
		}
		
		System.out.println("BUILD DECK:");
		System.out.println(deck);
	}
	
	private int reducePlayerAce() {
		while (playerSum > 21 && playerAcesCount > 0) {
			playerSum -= 10;
			playerAcesCount--;
		}
		return playerSum;
	}
	
	private int reduceDealerAce() {
		while (dealerSum > 21 && dealerAcesCount > 0) {
			dealerSum -= 10;
			dealerAcesCount--;
		}
		return dealerSum;
	}
}

