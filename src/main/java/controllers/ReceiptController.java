package controllers;

import api.CreateReceiptRequest;
import api.ReceiptResponse;
import api.TotalResponse;
import com.sun.corba.se.impl.oa.toa.TOA;
import dao.ReceiptDao;
import generated.tables.Total;
import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TotalRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("/receipts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReceiptController {
    final ReceiptDao receipts;

    public ReceiptController(ReceiptDao receipts) {
        this.receipts = receipts;
    }

    @POST
    public int createReceipt(@Valid @NotNull CreateReceiptRequest receipt) {
        return receipts.insert(receipt.merchant, receipt.amount);
    }

    @GET
    public List<TotalResponse> getReceipts() {
        List<TotalRecord> receiptRecords = receipts.getAllReceipts();
        return receiptRecords.stream().map(TotalResponse::new).collect(toList());
    }
}
