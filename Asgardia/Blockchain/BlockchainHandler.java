package Asgardia.Blockchain;

import java.io.*;
import java.util.*;
import java.util.concurrent.Future;

import org.bitcoinj.core.*;
import org.bitcoinj.core.listeners.*;
import org.bitcoinj.wallet.*;
import org.bitcoinj.wallet.listeners.*;

import com.google.common.util.concurrent.ListenableFuture;

import org.bitcoinj.store.*;

public class BlockchainHandler implements WalletCoinsReceivedEventListener, WalletCoinsSentEventListener, BlocksDownloadedEventListener, WalletFiles.Listener
{
	//Wallet localWallet;
	//WalletFiles walletFiles;
	PeerGroup peerGroup;
	
	public BlockchainHandler (PeerGroup p) {
		//walletFiles = f;
		//localWallet = f.getWallet () ;
		peerGroup = p;
		System.out.printf ("\tBlockchain wallet listening...\n") ;
	}

	
	@Override
	public void onCoinsReceived (Wallet wallet, Transaction trancecation, Coin prevBalance, Coin newBalance) {
		
		Coin balance = trancecation.getValueSentToMe (wallet) ;
		
		System.out.printf ("----收到一筆匯入----\n") ;
		System.out.printf ("\tTXID:%s\n", trancecation.getHash ().toString ()) ;
		System.out.printf ("\tMEMO:%s\n", trancecation.getMemo ()) ;
		System.out.printf ("\tRecvValue:%s\n", balance.toFriendlyString () ) ;
		System.out.printf ("\tWalletBalance:%s\n", newBalance.toFriendlyString () ) ;
		
		
		try {
			List<TransactionInput> txi =  trancecation.getInputs () ;
			List<TransactionOutput> txo = trancecation.getOutputs () ;
			for (TransactionInput xi : txi) {
				Address from = xi.getFromAddress () ;
				System.out.printf ("\tFrom:%s\n", from.toString () ) ;
			}
			
			for (TransactionOutput xo : txo) {
				Address to = xo.getAddressFromP2PKHScript (wallet.getParams ());
				System.out.printf ("\tTo:%s-%s\n", to.toString (), xo.getValue ().toPlainString () ) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace ();
		}
		
		System.out.printf ("-----------------\n") ;
		
	}

	@Override
	public void onCoinsSent (Wallet wallet, Transaction tx, Coin coin, Coin arg3) {
		System.out.printf ("send:%s coin\n", coin.toFriendlyString () ) ;
		System.out.printf ("tx : %s\n", tx.getHashAsString () ) ;
		
	}


	@Override
	public void onBlocksDownloaded (Peer peer, Block block, FilteredBlock arg2, int arg3) {
		//System.out.println (arg0) ;
		//System.out.println (arg1) ;
		System.out.printf ("區塊同步%s-%s\n", block.getTime (), block.getHashAsString () ) ;
	}


	@Override
	public void onAfterAutoSave (File file) {
		System.out.printf ("Wallet Auto-saved : %s\n", file.getPath () ) ;
		
	}


	@Override
	public void onBeforeAutoSave (File arg0) {
		// TODO Auto-generated method stub
		
	}
}
