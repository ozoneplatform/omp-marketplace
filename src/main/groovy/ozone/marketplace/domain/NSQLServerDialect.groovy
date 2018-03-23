package ozone.marketplace.domain

import java.sql.Types
import org.hibernate.dialect.SQLServerDialect

class NSQLServerDialect extends SQLServerDialect {
	
	public NSQLServerDialect() {
		super()
		registerColumnType(Types.CHAR, "nchar(1)")
		registerColumnType(Types.CLOB, "ntext")
		registerColumnType(Types.VARCHAR,'nvarchar($l)')
		registerColumnType(Types.LONGVARCHAR, 'nvarchar($l)')
	}
}
