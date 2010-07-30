/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openejb.core.timer;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.ejb.EJBException;
import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.apache.openejb.core.CoreDeploymentInfo;
import org.apache.openejb.core.ThreadContext;

public class TimerServiceWrapper implements TimerService {

    public TimerServiceWrapper() {
    }

    public Timer createTimer(Date initialExpiration, long intervalDuration, Serializable info) throws IllegalArgumentException, IllegalStateException, EJBException {
        return getTimerService().createTimer(initialExpiration, intervalDuration, info);
    }

    public Timer createTimer(Date expiration, Serializable info) throws IllegalArgumentException, IllegalStateException, EJBException {
        return getTimerService().createTimer(expiration, info);
    }

    public Timer createTimer(long initialDuration, long intervalDuration, Serializable info) throws IllegalArgumentException, IllegalStateException, EJBException {
        return getTimerService().createTimer(initialDuration, intervalDuration, info);
    }

    public Timer createTimer(long duration, Serializable info) throws IllegalArgumentException, IllegalStateException, EJBException {
        return getTimerService().createTimer(duration, info);
    }

    public Collection<Timer> getTimers() throws IllegalStateException, EJBException {
        return getTimerService().getTimers();
    }

    public Timer createSingleActionTimer(long l, TimerConfig timerConfig) throws IllegalArgumentException, IllegalStateException, EJBException {
        return getTimerService().createSingleActionTimer(l, timerConfig);
    }

    public Timer createSingleActionTimer(Date date, TimerConfig timerConfig) throws IllegalArgumentException, IllegalStateException, EJBException {
        return getTimerService().createSingleActionTimer(date, timerConfig);
    }

    public Timer createIntervalTimer(long l, long l1, TimerConfig timerConfig) throws IllegalArgumentException, IllegalStateException, EJBException {
        return getTimerService().createIntervalTimer(l, l1, timerConfig);
    }

    public Timer createIntervalTimer(Date date, long l, TimerConfig timerConfig) throws IllegalArgumentException, IllegalStateException, EJBException {
        return getTimerService().createIntervalTimer(date, l, timerConfig);
    }

    public Timer createCalendarTimer(ScheduleExpression scheduleExpression) throws IllegalArgumentException, IllegalStateException, EJBException {
        return getTimerService().createCalendarTimer(scheduleExpression);
    }

    public Timer createCalendarTimer(ScheduleExpression scheduleExpression, TimerConfig timerConfig) throws IllegalArgumentException, IllegalStateException, EJBException {
        return getTimerService().createCalendarTimer(scheduleExpression, timerConfig);
    }

    private TimerService getTimerService() throws IllegalStateException {
        ThreadContext threadContext = ThreadContext.getThreadContext();
        CoreDeploymentInfo deploymentInfo = threadContext.getDeploymentInfo();
        EjbTimerService timerService = deploymentInfo.getEjbTimerService();
        if (timerService == null) {
            throw new IllegalStateException("This ejb does not support timers " + deploymentInfo.getDeploymentID());
        } else if(deploymentInfo.getEjbTimeout() == null) {
            throw new IllegalStateException("This ejb does not support timers " + deploymentInfo.getDeploymentID() + " due to no timeout method is configured");
        }
        return new TimerServiceImpl(timerService, threadContext.getPrimaryKey(), deploymentInfo.getEjbTimeout());
    }
}
