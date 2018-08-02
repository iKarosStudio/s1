package Asgardia.Blockchain;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.bitcoinj.core.*;
import org.bitcoinj.params.*;
import org.bitcoinj.wallet.*;
import org.bitcoinj.store.*;
import org.bitcoinj.net.discovery.*;

import Asgardia.Server.*;

public class BlockchainMain extends Thread implements Runnable
{
	private static BlockchainMain instance ;
	
	NetworkParameters params;
	
	ECKey pKey;
	Address walletAddress;
	
	Wallet localWallet;
	WalletFiles walletFile;
	
	SPVBlockStore blockStore;
	BlockChain blockChain;
	PeerGroup peerGroup;
	
	public void run () {
		try {
			peerGroup.downloadBlockChain ();
			System.out.printf ("Download blockchain done\n") ;
			
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
	
	public static BlockchainMain getInstance () {
		if (instance == null) {
			instance = new BlockchainMain () ;
		}
		
		return instance;
	}
	
	public BlockchainMain () {
		try {
			System.out.println ("Blockchain initializing...") ;
			String WALLET_FILE_PATH = "./blockchain/wallet";
			String k = "TestHAHA";
			
			File w = new File (WALLET_FILE_PATH) ;
			if (w.exists () ) {
				System.out.printf ("\tFound wallet file\n") ;
				
				localWallet = Wallet.loadFromFile (w, null) ;
				walletFile = new WalletFiles (localWallet, new File (WALLET_FILE_PATH), 1000, TimeUnit.MICROSECONDS) ;
				
				localWallet = walletFile.getWallet () ;
				params = localWallet.getParams () ;
				
				List<ECKey> pKeys = localWallet.getImportedKeys () ;
				for (ECKey p : pKeys) {
					pKey = p;
					System.out.println ("imported key:" + p) ;
				}
				walletAddress = pKey.toAddress (params) ;
				
				//System.out.println (
				
			} else {
				//
			}
			
			//pKey = ECKey.fromPrivate (k.getBytes () ) ;
			System.out.printf ("\tPrivate key:%s\n", pKey.getPrivateKeyEncoded (params).toString () ) ;
			
			//walletAddress = pKey.toAddress (params) ;
			System.out.printf ("\tWallet address:%s\n", walletAddress.toString () ) ;
			
			//localWallet = new Wallet (params);
			//localWallet.importKey (pKey) ;
			//walletFileHandle = new WalletFiles (localWallet, walletFile, 1000, TimeUnit.MICROSECONDS) ;
			//walletFileHandle.saveNow ();
			
			
			File blockFile = new File ("./blockchain/tmp_file") ;
			blockStore = new SPVBlockStore (params, blockFile) ;
			
			blockChain = new BlockChain (params, localWallet, blockStore) ;
			peerGroup = new PeerGroup (params, blockChain) ;
			//peerGroup.addBlocksDownloadedEventListener ();
			peerGroup.addPeerDiscovery (new DnsDiscovery (params));
			peerGroup.addWallet (localWallet);
			peerGroup.setFastCatchupTimeSecs (localWallet.getEarliestKeyCreationTime () ) ;
			peerGroup.setBloomFilterFalsePositiveRate (0.00001) ;
			
			BlockchainHandler recvHandler = new BlockchainHandler (peerGroup) ;
			peerGroup.addBlocksDownloadedEventListener (recvHandler);
			
			peerGroup.start () ;
			System.out.printf ("\tStart peer group\n") ;
			
			
			//KernelThreadPool.getInstance ().execute (this) ;
			peerGroup.downloadBlockChain ();
			System.out.printf ("\tDownload blockchain done\n") ;
			
			walletFile.setListener (recvHandler) ;
			localWallet.autosaveToFile (w, 1000, TimeUnit.MICROSECONDS, recvHandler) ;
			localWallet.addCoinsReceivedEventListener (recvHandler) ;
			
			//Send
			System.out.printf ("Now Balance:%s\n", localWallet.getWatchedBalance ().toPlainString ()) ;
			try {
			//if (localWallet.getWatchedBalance ().) {
				Coin amountToSend = Coin.valueOf (5, 20) ;
				Address toAddress = Address.fromBase58(params, "mjrbPSym3oAR4mHYVcvLq6UjBZSFMLMatL");
				localWallet.sendCoins(peerGroup, toAddress, amountToSend) ;
			//}
				
			} catch (InsufficientMoneyException e) {
				System.out.printf ("%s\n", e.toString ()) ;
			}
			
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}
}
