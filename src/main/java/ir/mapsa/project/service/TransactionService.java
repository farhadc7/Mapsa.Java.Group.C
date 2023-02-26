package ir.mapsa.project.service;

import ir.mapsa.project.entity.Customer;
import ir.mapsa.project.entity.Transaction;
import ir.mapsa.project.repository.CustomerRepository;
import ir.mapsa.project.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionService implements TransactionServiceBase {
    // transction issue has remained. how can we fix it ???
    TransactionRepository transactionRepository;
    CustomerRepository customerRepository;

    public TransactionService() {
        transactionRepository=new TransactionRepository();
        customerRepository=new CustomerRepository();
    }

    @Override
    public void resolveTransaction(Transaction transaction) throws Exception {
        if(!withdraw(transaction.getSenderCardNumber(),transaction.getAmount())){
            return;
        }

         deposit(transaction.getRecieverCardNumber(),transaction.getAmount());
        // deposit can be failed ...
        transactionRepository.add(transaction);
    }
    // throw the exact exception
    private Boolean withdraw(String cardNumber,Long amount) throws Exception {
        //bad practice to get all customers and find the one you needed.
        List<Customer> customers;
        customers=customerRepository.getAll();
        Customer customer=customers.stream().filter(a->a.getCardNo().equals(cardNumber)).findFirst().get();
        if(customer.getBalance()<amount){
            return  false;
        }

        customer.setBalance(customer.getBalance()-amount);
        customerRepository.update(customer);
        return true;
    }
       
    private Boolean deposit(String cardNumber,Long amount) throws Exception {
        List<Customer> customers;
        customers=customerRepository.getAll();
        Customer customer=customers.stream().filter(a->a.getCardNo().equals(cardNumber)).findFirst().get();

        customer.setBalance(customer.getBalance()+amount);
        customerRepository.update(customer);

        return true;
    }
}
