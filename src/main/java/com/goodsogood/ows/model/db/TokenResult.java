package com.goodsogood.ows.model.db;

import lombok.Data;
import javax.persistence.Table;


@Data
@Table(name = "Token_Result")
public class TokenResult {
     public  Integer Code;
}
