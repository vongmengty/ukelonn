/*
 * Copyright 2019 Steinar Bang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package no.priv.bang.ukelonn.api.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.priv.bang.ukelonn.UkelonnService;
import no.priv.bang.ukelonn.beans.SumYear;
import no.priv.bang.ukelonn.beans.SumYearMonth;

@Path("/statistics")
@Produces(MediaType.APPLICATION_JSON)
public class Statistics {

    @Inject
    UkelonnService ukelonn;

    @GET
    @Path("/earnings/sumoveryear/{username}")
    public List<SumYear> earningsSumOverYear(@PathParam("username") String username) {
        return ukelonn.earningsSumOverYear(username);
    }

    @GET
    @Path("/earnings/sumovermonth/{username}")
    public List<SumYearMonth> earningsSumOverMonth(@PathParam("username") String username) {
        return ukelonn.earningsSumOverMonth(username);
    }

}
