import React from 'react';

function JobtypesBox(props) {
    const {id, className, jobtypes, jobtypesMap, value, account, performedjob, onJobtypeFieldChange } = props;
    return (
        <select multiselect="true" size="10" id={id} className={className} onChange={(event) => onJobtypeFieldChange(event.target.value, jobtypesMap, account, performedjob)} value={value}>
          {jobtypes.map((val) => <option key={val.id}>{val.transactionTypeName}</option>)}
        </select>
    );
}

export default JobtypesBox;