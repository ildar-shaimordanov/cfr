package org.benf.cfr.reader.bytecode.analysis.parse.lvalue;

import org.benf.cfr.reader.bytecode.analysis.parse.Expression;
import org.benf.cfr.reader.bytecode.analysis.parse.LValue;
import org.benf.cfr.reader.bytecode.analysis.parse.StatementContainer;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.LValueAssignmentCollector;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.LValueRewriter;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.SSAIdentifierFactory;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.SSAIdentifiers;
import org.benf.cfr.reader.bytecode.analysis.stack.StackEntry;

/**
 * Created by IntelliJ IDEA.
 * User: lee
 * Date: 15/03/2012
 * Time: 18:25
 * To change this template use File | Settings | File Templates.
 */
public class StackSSALabel implements LValue {
    private final long id;
    private final StackEntry stackEntry;

    public StackSSALabel(long id, StackEntry stackEntry) {
        this.id = id;
        this.stackEntry = stackEntry;
    }

    @Override
    public String toString() {
        return "v" + id;
    }

    @Override
    public int getNumberOfCreators() {
        return stackEntry.getSourceCount();
    }

    /*
     * Can any use of this be replaced with the RHS instead?
     * (Assuming that values in the RHS are not mutated)
     */
    @Override
    public void determineLValueEquivalence(Expression rhsAssigned, StatementContainer statementContainer, LValueAssignmentCollector lValueAssigmentCollector) {
        if (getNumberOfCreators() == 1) {
            if ((rhsAssigned.isSimple() || stackEntry.getUsageCount() == 1)) {
                lValueAssigmentCollector.collect(this, statementContainer, rhsAssigned);
            } else if (stackEntry.getUsageCount() > 1) {
                lValueAssigmentCollector.collectMultiUse(this, statementContainer, rhsAssigned);
            }
        }
    }

    @Override
    public SSAIdentifiers collectVariableMutation(SSAIdentifierFactory ssaIdentifierFactory) {
        return new SSAIdentifiers();
    }

    @Override
    public LValue replaceSingleUsageLValues(LValueRewriter lValueRewriter, SSAIdentifiers ssaIdentifiers, StatementContainer statementContainer) {
        return this;
    }

    public StackEntry getStackEntry() {
        return stackEntry;
    }

}
